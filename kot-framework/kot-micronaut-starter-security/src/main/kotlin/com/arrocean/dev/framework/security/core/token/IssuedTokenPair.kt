package com.arrocean.dev.framework.security.core.token

/**
 * 登录成功后签发的一组令牌。
 */
data class IssuedTokenPair(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val accessTokenExpireSeconds: Long,
    val refreshTokenExpireSeconds: Long,
    val sessionId: String,
)

