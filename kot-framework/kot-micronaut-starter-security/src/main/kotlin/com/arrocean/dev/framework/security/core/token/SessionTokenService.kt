package com.arrocean.dev.framework.security.core.token

import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.security.config.SecurityProperties
import com.arrocean.dev.framework.security.core.context.LoginUser
import io.lettuce.core.api.StatefulRedisConnection
import io.micronaut.json.JsonMapper
import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton
import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * 会话令牌服务。
 */
interface SessionTokenService {

    /**
     * 为登录用户签发 access/refresh token，并将会话写入 Redis。
     */
    fun issueTokens(loginUser: LoginUser, clientId: String? = null): IssuedTokenPair

    /**
     * 按 access token 撤销会话。
     *
     * @return 被撤销的会话摘要；若 access token 不存在则返回 null
     */
    fun revokeByAccessToken(accessToken: String): RevokedSessionToken?
}

@Singleton
open class RedisSessionTokenService(
    private val tokenService: TokenService,
    private val securityProperties: SecurityProperties,
    private val secureRandom: SecureRandom,
    private val redisConnection: StatefulRedisConnection<String, String>,
    private val jsonMapper: JsonMapper,
    private val clock: Clock,
) : SessionTokenService {

    override fun issueTokens(loginUser: LoginUser, clientId: String?): IssuedTokenPair {
        validateProperties()

        val normalizedClientId = clientId?.trim()?.takeIf(String::isNotBlank) ?: securityProperties.defaultClientId
        val sessionId = generateOpaqueToken(18)
        val refreshToken = generateOpaqueToken(32)
        val accessLoginUser = loginUser.copy(sessionId = sessionId)

        val accessToken = tokenService.generateAccessToken(accessLoginUser)
        val accessTtlSeconds = securityProperties.accessTokenExpireSeconds.toLong()
        val refreshTtlSeconds = securityProperties.refreshTokenExpireSeconds.toLong()
        val now = Instant.now(clock)

        val sessionRecord = SessionTokenRecord(
            sessionId = sessionId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = accessLoginUser.id,
            userType = accessLoginUser.userTypeValue,
            tenantId = accessLoginUser.tenantId,
            username = accessLoginUser.username,
            clientId = normalizedClientId,
            scopes = accessLoginUser.scopes.toList(),
            userInfo = buildUserInfo(accessLoginUser),
            issuedAtEpochSecond = now.epochSecond,
            accessTokenExpiresAtEpochSecond = now.plusSeconds(accessTtlSeconds).epochSecond,
            refreshTokenExpiresAtEpochSecond = now.plusSeconds(refreshTtlSeconds).epochSecond,
        )

        val payload = serializeSessionRecord(sessionRecord)
        persistSessionRecord(sessionId, accessToken, refreshToken, accessTtlSeconds, refreshTtlSeconds, payload)

        return IssuedTokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpireSeconds = accessTtlSeconds,
            refreshTokenExpireSeconds = refreshTtlSeconds,
            sessionId = sessionId,
        )
    }

    override fun revokeByAccessToken(accessToken: String): RevokedSessionToken? {
        val normalizedAccessToken = accessToken.trim()
        if (normalizedAccessToken.isBlank()) {
            return null
        }

        return runCatching {
            val commands = redisConnection.sync()
            val sessionId = commands.get(accessKey(normalizedAccessToken)) ?: return@runCatching null
            val sessionPayload = commands.get(sessionKey(sessionId))
            if (sessionPayload.isNullOrBlank()) {
                commands.del(accessKey(normalizedAccessToken))
                return@runCatching null
            }

            val sessionRecord = deserializeSessionRecord(sessionPayload)
            commands.del(
                accessKey(sessionRecord.accessToken),
                refreshKey(sessionRecord.refreshToken),
                sessionKey(sessionRecord.sessionId),
            )

            RevokedSessionToken(
                sessionId = sessionRecord.sessionId,
                accessToken = sessionRecord.accessToken,
                refreshToken = sessionRecord.refreshToken,
                userId = sessionRecord.userId,
                userType = sessionRecord.userType,
                tenantId = sessionRecord.tenantId,
                username = sessionRecord.username,
                clientId = sessionRecord.clientId,
                scopes = sessionRecord.scopes.toSet(),
            )
        }.getOrElse { ex ->
            throw ServiceExceptionFactory.internalServerError("撤销 Redis 登录会话失败: {}", ex.message ?: "unknown")
        }
    }

    private fun validateProperties() {
        require(securityProperties.accessTokenExpireSeconds > 0) { "accessTokenExpireSeconds 必须大于 0" }
        require(securityProperties.refreshTokenExpireSeconds > 0) { "refreshTokenExpireSeconds 必须大于 0" }
        require(securityProperties.defaultClientId.isNotBlank()) { "defaultClientId 不能为空" }
        require(securityProperties.redis.keyPrefix.isNotBlank()) { "redis.keyPrefix 不能为空" }
    }

    private fun serializeSessionRecord(sessionRecord: SessionTokenRecord): String {
        return runCatching {
            jsonMapper.writeValueAsString(sessionRecord)
        }.getOrElse { ex ->
            throw ServiceExceptionFactory.internalServerError("序列化登录会话失败: {}", ex.message ?: "unknown")
        }
    }

    private fun deserializeSessionRecord(payload: String): SessionTokenRecord {
        return runCatching {
            jsonMapper.readValue(payload, SessionTokenRecord::class.java)
                ?: throw IllegalStateException("反序列化结果为空")
        }.getOrElse { ex ->
            throw ServiceExceptionFactory.internalServerError("反序列化登录会话失败: {}", ex.message ?: "unknown")
        }
    }

    private fun persistSessionRecord(
        sessionId: String,
        accessToken: String,
        refreshToken: String,
        accessTtlSeconds: Long,
        refreshTtlSeconds: Long,
        payload: String,
    ) {
        runCatching {
            val commands = redisConnection.sync()
            commands.setex(sessionKey(sessionId), refreshTtlSeconds, payload)
            commands.setex(accessKey(accessToken), accessTtlSeconds, sessionId)
            commands.setex(refreshKey(refreshToken), refreshTtlSeconds, sessionId)
        }.getOrElse { ex ->
            throw ServiceExceptionFactory.internalServerError("写入 Redis 登录会话失败: {}", ex.message ?: "unknown")
        }
    }

    private fun buildUserInfo(loginUser: LoginUser): Map<String, String> {
        return linkedMapOf<String, String>().apply {
            put("username", loginUser.username)
            put("nickname", loginUser.nickname)
            put("tenantId", loginUser.tenantId.toString())
            loginUser.deptId?.let { put("deptId", it.toString()) }
            loginUser.email?.let { put("email", it) }
            loginUser.mobile?.let { put("mobile", it) }
            loginUser.sessionId?.let { put("sessionId", it) }
        }
    }

    private fun sessionKey(sessionId: String): String = "${securityProperties.redis.keyPrefix}:session:$sessionId"

    private fun accessKey(accessToken: String): String = "${securityProperties.redis.keyPrefix}:access:$accessToken"

    private fun refreshKey(refreshToken: String): String = "${securityProperties.redis.keyPrefix}:refresh:$refreshToken"

    private fun generateOpaqueToken(size: Int): String {
        val bytes = ByteArray(size)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}

@Serdeable
data class SessionTokenRecord(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val userType: Int,
    val tenantId: Long,
    val username: String,
    val clientId: String,
    val scopes: List<String>,
    val userInfo: Map<String, String>,
    val issuedAtEpochSecond: Long,
    val accessTokenExpiresAtEpochSecond: Long,
    val refreshTokenExpiresAtEpochSecond: Long,
)

@Serdeable
data class RevokedSessionToken(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val userType: Int,
    val tenantId: Long,
    val username: String,
    val clientId: String,
    val scopes: Set<String>,
)
