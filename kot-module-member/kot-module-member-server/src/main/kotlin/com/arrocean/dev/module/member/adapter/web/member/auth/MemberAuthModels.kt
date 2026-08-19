package com.arrocean.dev.module.member.adapter.web.member.auth

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Serdeable
data class MemberLoginRequest(
    @field:NotBlank(message = "用户名或手机号不能为空")
    @field:Size(max = 32, message = "用户名或手机号长度不能超过 32 位")
    val usernameOrMobile: String,
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    val password: String,
)

@Serdeable
data class MemberRegisterByUsernameRequest(
    @field:NotBlank(message = "用户名不能为空")
    @field:Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,31}$", message = "用户名格式不正确")
    val username: String,
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    val password: String,
)

@Serdeable
data class MemberRegisterByMobileRequest(
    @field:NotBlank(message = "手机号不能为空")
    @field:Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    val mobile: String,
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    val password: String,
)

@Serdeable
data class MemberLoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val refreshToken: String? = null,
    val expiresInSeconds: Long? = null,
    val sessionId: String? = null,
)
