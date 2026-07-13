package com.arrocean.dev.module.system.adapter.web.admin.auth

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Pattern

/**
 * 管理员登录请求。
 *
 * 说明：
 * - 当前阶段要求用户名、密码，可选附带客户端标识
 * - captcha / tenantId 先预留为可选字段，避免过早绑定具体方案
 *
 * @author WhiteSprite
 */
@Schema(description = "管理员登录请求")
@Serdeable
data class AdminLoginRequest(
    @field:NotBlank(message = "用户名不能为空")
    @field:Size(min = 3, max = 32, message = "用户名长度必须在 3 到 32 位之间")
    @field:Schema(description = "用户名", example = "admin")
    val username: String,

    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    @field:Schema(description = "密码", example = "123456")
    val password: String,

    @field:Schema(description = "租户编号（单租户阶段可为空）")
    val tenantId: Long? = null,

    @field:Schema(description = "验证码编码（暂未启用）")
    val captchaCode: String? = null,

    @field:Schema(description = "验证码标识（暂未启用）")
    val captchaUuid: String? = null,
)

/**
 * 管理员登录响应。
 *
 * @author WhiteSprite
 */
@Schema(description = "管理员登录响应")
@Serdeable
data class AdminLoginResponse(
    @field:Schema(description = "访问令牌（JWT）", example = "eyJhbGciOiJIUzI1NiJ9...")
    val accessToken: String,

    @field:Schema(description = "令牌类型", example = "Bearer")
    val tokenType: String = "Bearer",

    @field:Schema(description = "刷新令牌")
    val refreshToken: String? = null,

    @field:Schema(description = "accessToken 剩余有效期（秒）")
    val expiresInSeconds: Long? = null,

    @field:Schema(description = "会话编号")
    val sessionId: String? = null,
)

/**
 * 当前认证用户信息响应。
 *
 * @author WhiteSprite
 */
@Schema(description = "当前认证用户信息")
@Serdeable
data class AdminAuthProfileResponse(
    @field:Schema(description = "用户 ID", example = "1")
    val id: Long,

    @field:Schema(description = "用户类型编码", example = "1")
    val userType: Int,

    @field:Schema(description = "用户类型名称", example = "管理员")
    val userTypeName: String,

    @field:Schema(description = "用户名", example = "admin")
    val username: String,

    @field:Schema(description = "昵称", example = "管理员")
    val nickname: String,

    @field:Schema(description = "部门 ID")
    val deptId: Long? = null,

    @field:Schema(description = "邮箱", example = "admin@example.com")
    val email: String? = null,

    @field:Schema(description = "手机号", example = "13800138000")
    val mobile: String? = null,

    @field:Schema(description = "租户 ID", example = "1")
    val tenantId: Long,

    @field:Schema(description = "授权范围")
    val scopes: Set<String> = emptySet(),

    @field:Schema(description = "会话编号")
    val sessionId: String? = null,
)

/**
 * 管理员注册请求。
 *
 * @author WhiteSprite
 */
@Serdeable
data class AdminRegisterRequest(
    /**
     * 用户名
     */
    @field:NotBlank(message = "用户名不能为空")
    @field:Size(min = 3, max = 32, message = "用户名长度必须在 3 到 32 位之间")
    @field:Pattern(
        regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,31}$",
        message = "用户名只能包含字母、数字和下划线，且必须以字母开头"
    )
    val username: String,

    /**
     * 密码
     */
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    val password: String,

    /**
     * 昵称。不传则默认取用户名。
     */
    val nickname: String? = null,
)


