package com.whitesprite.dev.module.system.adapter.web.admin.auth

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 管理员登录请求。
 *
 * 说明：
 * - 当前阶段只要求用户名和密码
 * - captcha / tenantId 先预留为可选字段，避免过早绑定具体方案
 *
 * @author WhiteSprite
 */
@Serdeable
data class AdminLoginRequest(
    /**
     * 用户名
     */
    @field:NotBlank(message = "用户名不能为空")
    @field:Size(min = 3, max = 32, message = "用户名长度必须在 3 到 32 位之间")
    val username: String,

    /**
     * 密码
     */
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 128, message = "密码长度必须在 6 到 128 位之间")
    val password: String,

    /**
     * 租户编号。
     *
     * 单租户阶段可为空，多租户登录时再接入。
     */
    val tenantId: Long? = null,

    /**
     * 验证码编码。
     *
     * 第一阶段暂未启用验证码，可为空。
     */
    val captchaCode: String? = null,

    /**
     * 验证码标识。
     *
     * 第一阶段暂未启用验证码，可为空。
     */
    val captchaUuid: String? = null,
)

/**
 * 管理员登录响应。
 *
 * @author WhiteSprite
 */
@Serdeable
data class AdminLoginResponse(
    /**
     * 访问令牌
     */
    val accessToken: String,

    /**
     * 令牌类型
     */
    val tokenType: String = "Bearer",

    /**
     * 刷新令牌。
     *
     * 当前阶段暂未启用，先保留为空扩展位。
     */
    val refreshToken: String? = null,

    /**
     * Access Token 剩余有效期（秒）。
     *
     * 当前阶段暂未回填，先保留为空扩展位。
     */
    val expiresInSeconds: Long? = null,

    /**
     * 会话编号。
     *
     * 当未来引入 Redis / 会话持久化后可用于会话管理。
     */
    val sessionId: String? = null,
)

/**
 * 当前认证用户信息响应。
 *
 * @author WhiteSprite
 */
@Serdeable
data class AdminAuthProfileResponse(
    /**
     * 用户 ID
     */
    val id: Long,

    /**
     * 用户类型编码
     */
    val userType: Int,

    /**
     * 用户类型名称
     */
    val userTypeName: String,

    /**
     * 用户名
     */
    val username: String,

    /**
     * 昵称
     */
    val nickname: String,

    /**
     * 部门 ID
     */
    val deptId: Long? = null,

    /**
     * 邮箱
     */
    val email: String? = null,

    /**
     * 手机号
     */
    val mobile: String? = null,

    /**
     * 租户 ID
     */
    val tenantId: Long,

    /**
     * 授权范围
     */
    val scopes: Set<String> = emptySet(),

    /**
     * 会话编号
     */
    val sessionId: String? = null,
)

