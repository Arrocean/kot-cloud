package com.whitesprite.dev.module.system.application.auth.core.facade

import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.security.core.context.CurrentLoginUserProvider
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthAssembler
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthProfileResponse
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginRequest
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginResponse
import com.whitesprite.dev.module.system.application.user.core.query.GetUserQuery
import com.whitesprite.dev.module.system.application.user.core.query.UserQueryHandler
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.enums.logger.LoginLogTypeEnum
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
open class AdminAuthService(
    private val currentLoginUserProvider: CurrentLoginUserProvider,
    private val userQueryHandler: UserQueryHandler
) {

    /**
     * 管理员登录。
     */
    open fun login(req: AdminLoginRequest): AdminLoginResponse {
        // 校验验证码
        // TODO varifyCaptcha(req.captchaCode)

        // 校验获取用户
        val user = authenticate(req.username, req.password)

        // 社交用户相关
//        if(req.socialType != null) {
//            // TODO WhiteSprite：后续补充社交登录相关逻辑
//            throw ServiceExceptionFactory.notImplemented("社交登录待实现")
//        }

        // 创建Token，记录登录日志
//        return createTokenAfterLoginSuccess()

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

    open fun authenticate(username: String, password: String): AdminUser {
        val LoginLogTypeEnum = LoginLogTypeEnum.LOGIN_PASSWORD
        // 获取用户
        val query = GetUserQuery
//        val user = userQueryHandler.handle(GetUserQuery.)
//            ?: throw ServiceExceptionFactory.exception("用户不存在")

        // TODO 校验密码

        throw ServiceExceptionFactory.notImplemented(
            "管理员登录链路待实现：后续补充用户校验、密码比对与 Token 签发"
        )
    }
}

