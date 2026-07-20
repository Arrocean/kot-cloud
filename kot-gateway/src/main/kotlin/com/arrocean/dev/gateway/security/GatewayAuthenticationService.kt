package com.arrocean.dev.gateway.security

import jakarta.inject.Singleton

/** 已完成 JWT 验签后可用于会话校验的最小认证主体。 */
data class GatewayPrincipal(
    val token: String,
    val sessionId: String,
)

/** Gateway JWT 验签抽象，隔离过滤器与具体 JWT 库。 */
fun interface GatewayTokenVerifier {
    /**
     * 验证 JWT 并提取用于会话校验的主体信息。
     *
     * @param token 不含 Bearer 前缀的原始 JWT
     * @return 已通过签名和 claim 校验的认证主体
     * @throws GatewayAuthenticationException Token 无效或缺少必要 claim 时抛出
     */
    fun verify(token: String): GatewayPrincipal
}

/** Gateway Redis 会话有效性校验抽象。 */
fun interface GatewaySessionValidator {
    /**
     * 判断会话是否仍存在于权威会话存储中。
     *
     * @param sessionId JWT 中的会话标识
     * @return 会话仍有效时返回 true
     */
    fun isActive(sessionId: String): Boolean
}

/** 表示 Token 缺失、格式错误、验签失败或会话失效的认证异常。 */
class GatewayAuthenticationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * 协调 Bearer Token 解析、JWT 验签和 Redis 会话校验。
 *
 * 网关仅校验当前请求，不保存用户上下文；原始 Authorization 由代理继续传递给下游。
 */
@Singleton
class GatewayAuthenticationService(
    private val tokenVerifier: GatewayTokenVerifier,
    private val sessionValidator: GatewaySessionValidator,
) {

    /**
     * 完成当前请求的 Token 解析、验签和会话有效性校验。
     *
     * @param authorization 入站 Authorization 请求头
     * @return 已验证的最小认证主体
     * @throws GatewayAuthenticationException 缺少 Token、验签失败或会话已失效时抛出
     */
    fun authenticate(authorization: String?): GatewayPrincipal {
        val token = BearerTokenReader.extract(authorization)
            ?: throw GatewayAuthenticationException("Missing bearer token")
        val principal = tokenVerifier.verify(token)
        if (!sessionValidator.isActive(principal.sessionId)) {
            throw GatewayAuthenticationException("Session is not active")
        }
        return principal
    }
}
