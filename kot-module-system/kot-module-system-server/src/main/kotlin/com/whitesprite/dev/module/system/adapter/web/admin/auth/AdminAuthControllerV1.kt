package com.whitesprite.dev.module.system.adapter.web.admin.auth

import com.whitesprite.dev.framework.common.http.ApiPrefix
import com.whitesprite.dev.framework.common.poko.CommonResult
import com.whitesprite.dev.framework.common.poko.success
import com.whitesprite.dev.module.system.application.auth.core.facade.AdminAuthAppService
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
 * 暂不在这里绑定验证码、刷新令牌、Redis 会话等实现方案，
 * 等认证链路跑通后再逐步补充。
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
    private val authAppService: AdminAuthAppService,
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