package com.arrocean.dev.module.system.adapter.web.admin.auth

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.module.system.application.auth.core.facade.AdminAuthService
import com.arrocean.dev.module.system.adapter.config.AdminLoginSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.AdminProfileSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.BooleanSuccessResponseSchema
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

/**
 * 后台管理端认证控制器。
 *
 * 第一阶段只声明最小闭环接口：
 * - 登录
 * - 登出
 * - 获取当前登录用户信息
 *
 * 当前阶段已接入基础的 refresh/access token 签发与登录会话落 Redis；
 * 验证码、单点登录等能力后续再继续扩展。
 *
 * @author WhiteSprite
 */
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "认证管理")
@Controller(ApiPrefix.ADMIN_V1 + "/system/auth")
open class AdminAuthControllerV1(
    /**
     * 认证应用服务
     */
    private val authAppService: AdminAuthService,
) {

    /**
     * 管理员登录。
     */
    @Operation(summary = "管理员登录", description = "用户名密码登录，返回 JWT accessToken")
    @ApiResponse(
        responseCode = "200",
        description = "登录成功，返回 token",
        content = [Content(schema = Schema(implementation = AdminLoginSuccessResponseSchema::class))],
    )
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/login")
    open suspend fun login(@Body @Valid req: AdminLoginRequest): CommonResult<AdminLoginResponse> {
        return success(authAppService.login(req))
    }

    /**
     * 管理员登出。
     */
    @Operation(summary = "管理员登出", description = "使当前 accessToken 失效，清除 Redis 会话")
    @ApiResponse(
        responseCode = "200",
        description = "登出成功",
        content = [Content(schema = Schema(implementation = BooleanSuccessResponseSchema::class))],
    )
    @SecurityRequirement(name = "bearerAuth")
    @Post("/logout")
    open suspend fun logout(): CommonResult<Boolean> {
        authAppService.logout()
        return success(true)
    }

    /**
     * 获取当前登录用户信息。
     */
    @Operation(summary = "获取当前用户信息", description = "根据当前 accessToken 获取登录用户的基本信息与权限范围")
    @ApiResponse(
        responseCode = "200",
        description = "用户信息",
        content = [Content(schema = Schema(implementation = AdminProfileSuccessResponseSchema::class))],
    )
    @SecurityRequirement(name = "bearerAuth")
    @Get("/me")
    open fun getProfile(): CommonResult<AdminAuthProfileResponse> {
        return success(authAppService.getProfile())
    }

    /**
     * 管理员注册。注册成功后自动登录，返回 accessToken。
     */
    @Operation(summary = "管理员注册", description = "注册新管理员账号，成功后自动登录并返回 JWT accessToken")
    @ApiResponse(
        responseCode = "200",
        description = "注册并登录成功，返回 token",
        content = [Content(schema = Schema(implementation = AdminLoginSuccessResponseSchema::class))],
    )
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/register")
    open suspend fun register(@Body @Valid req: AdminRegisterRequest): CommonResult<AdminLoginResponse> {
        return success(authAppService.register(req))
    }

}

