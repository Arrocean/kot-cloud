package com.arrocean.dev.module.system.adapter.web.admin.auth

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.module.system.application.auth.core.facade.AdminAuthService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
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
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/login")
    open fun login(@Body @Valid req: AdminLoginRequest): CommonResult<AdminLoginResponse> {
        return success(authAppService.login(req))
    }

    /**
     * 管理员登出。
     */
    @Post("/logout")
    open fun logout(): CommonResult<Boolean> {
        authAppService.logout()
        return success(true)
    }

    /**
     * 获取当前登录用户信息。
     */
    @Get("/me")
    open fun getProfile(): CommonResult<AdminAuthProfileResponse> {
        return success(authAppService.getProfile())
    }

}

