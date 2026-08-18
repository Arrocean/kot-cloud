package com.arrocean.dev.framework.security.core.token

import com.arrocean.dev.framework.common.enums.toCommonUserTypeEnum
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.security.config.SecurityProperties
import com.arrocean.dev.framework.security.core.context.LoginUser
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.generator.TokenGenerator
import jakarta.inject.Singleton

/**
 * 基于 Micronaut JWT 的 Token 服务实现。
 *
 * @author WhiteSprite
 */
@Singleton
open class JwtTokenService(
    private val tokenGenerator: TokenGenerator,
    private val securityProperties: SecurityProperties
) : TokenService {

    override fun generateAccessToken(loginUser: LoginUser): String {
        val authentication = Authentication.build(
            loginUser.username,
            buildRoles(loginUser),
            buildAttributes(loginUser)
        )
        return tokenGenerator.generateToken(authentication, securityProperties.accessTokenExpireSeconds)
            .orElseThrow {
                ServiceExceptionFactory.internalServerError("生成 Access Token 失败")
            }
    }

    override fun toLoginUser(authentication: Authentication): LoginUser? {
        val attributes = authentication.attributes
        val username = authentication.name.ifBlank {
            attributes[SecurityClaimNames.NICKNAME]?.toString().orEmpty()
        }
        if (username.isBlank()) {
            return null
        }

        val userId = attributes[SecurityClaimNames.USER_ID].asLongOrNull() ?: return null
        val userTypeValue = attributes[SecurityClaimNames.USER_TYPE].asIntOrNull() ?: return null
        val userType = userTypeValue.toCommonUserTypeEnum() ?: return null
        val tenantId = attributes[SecurityClaimNames.TENANT_ID].asLongOrNull() ?: return null

        return LoginUser(
            id = userId,
            userType = userType,
            username = username,
            nickname = attributes[SecurityClaimNames.NICKNAME]?.toString() ?: username,
            deptId = attributes[SecurityClaimNames.DEPT_ID].asLongOrNull(),
            email = attributes[SecurityClaimNames.EMAIL]?.toString(),
            mobile = attributes[SecurityClaimNames.MOBILE]?.toString(),
            tenantId = tenantId,
            scopes = resolveScopes(attributes[SecurityClaimNames.SCOPES], authentication.roles, userType.name),
            sessionId = attributes[SecurityClaimNames.SESSION_ID]?.toString(),
        )
    }

    private fun buildAttributes(loginUser: LoginUser): Map<String, Any> {
        val attributes = linkedMapOf<String, Any>(
            SecurityClaimNames.USER_ID to loginUser.id,
            SecurityClaimNames.USER_TYPE to loginUser.userType.value,
            SecurityClaimNames.NICKNAME to loginUser.nickname,
            SecurityClaimNames.TENANT_ID to loginUser.tenantId,
        )
        loginUser.deptId?.let { attributes[SecurityClaimNames.DEPT_ID] = it }
        loginUser.email?.let { attributes[SecurityClaimNames.EMAIL] = it }
        loginUser.mobile?.let { attributes[SecurityClaimNames.MOBILE] = it }
        if (loginUser.scopes.isNotEmpty()) {
            attributes[SecurityClaimNames.SCOPES] = loginUser.scopes.toList()
        }
        loginUser.sessionId?.let { attributes[SecurityClaimNames.SESSION_ID] = it }
        return attributes
    }

    private fun buildRoles(loginUser: LoginUser): Collection<String> {
        val roles = LinkedHashSet<String>()
        roles += loginUser.userType.name
        roles += loginUser.scopes
        return roles
    }

    private fun resolveScopes(rawScopes: Any?, roles: Collection<String>, userTypeRole: String): Set<String> {
        val scopes = rawScopes.asStringSet()
        if (scopes.isNotEmpty()) {
            return scopes
        }
        return roles.filterNot { it == userTypeRole }.toSet()
    }
}

private fun Any?.asLongOrNull(): Long? = when (this) {
    is Long -> this
    is Int -> this.toLong()
    is Short -> this.toLong()
    is Number -> this.toLong()
    is String -> this.toLongOrNull()
    else -> null
}

private fun Any?.asIntOrNull(): Int? = when (this) {
    is Int -> this
    is Short -> this.toInt()
    is Long -> this.toInt()
    is Number -> this.toInt()
    is String -> this.toIntOrNull()
    else -> null
}

private fun Any?.asStringSet(): Set<String> = when (this) {
    is Collection<*> -> this.mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }.toSet()
    is Array<*> -> this.mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }.toSet()
    is String -> this.split(',').mapNotNull { it.trim().takeIf(String::isNotBlank) }.toSet()
    else -> emptySet()
}


