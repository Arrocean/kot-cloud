package com.whitesprite.dev.module.system.application.auth.core.facade

import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.security.core.context.CurrentLoginUserProvider
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthAssembler
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthProfileResponse
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginRequest
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginResponse
import jakarta.inject.Singleton

/**
 * 后台管理端认证应用服务。
 *
 * 说明：
 * - 当前阶段先提供控制器接口骨架所需的最小门面
 * - 登录 / 登出流程后续再接入用户查询、密码校验、Token 签发、会话持久化等能力
 *
 * @author WhiteSprite
 */
@Singleton
open class AdminAuthAppService(
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {

    /**
     * 管理员登录。
     */
    open fun login(req: AdminLoginRequest): AdminLoginResponse {
        // 校验验证码
        // TODO varifyCaptcha(req.captchaCode)

        // 校验获取用户
//        val loginUser = authenticate(req.username, req.password)

        throw ServiceExceptionFactory.notImplemented(
            "管理员登录链路待实现：后续补充用户校验、密码比对与 Token 签发"
        )
    }

    /**
     * 管理员登出。
     */
    open fun logout() {
        // TODO WhiteSprite：当引入 Redis / 会话持久化或 Token 黑名单后，在这里真正注销登录态
        throw ServiceExceptionFactory.notImplemented(
            "管理员登出链路待实现：后续补充会话撤销或 Token 失效处理"
        )
    }

    /**
     * 获取当前登录用户信息。
     */
    open fun getProfile(): AdminAuthProfileResponse {
        val loginUser = currentLoginUserProvider.requireLoginUser()
        return AdminAuthAssembler.toProfileResponse(loginUser)
    }
}

