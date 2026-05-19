package com.arrocean.dev.module.system.application.auth.core.facade

import com.arrocean.dev.framework.common.enums.CommonUserTypeEnum
import com.arrocean.dev.framework.common.enums.isDisable
import com.arrocean.dev.framework.common.exception.ServiceException
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.common.util.web.WebUtils
import com.arrocean.dev.framework.security.core.context.LoginUser
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.framework.security.core.password.PasswordEncoder
import com.arrocean.dev.framework.security.core.token.SessionTokenService
import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminAuthAssembler
import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminAuthProfileResponse
import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminLoginRequest
import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminLoginResponse
import com.arrocean.dev.module.system.adapter.web.admin.user.UpdateUserRequest
import com.arrocean.dev.module.system.application.log.core.facade.LoginLogService
import com.arrocean.dev.module.system.application.user.core.command.LoginUserCommand
import com.arrocean.dev.module.system.application.user.core.command.UserCommandHandler
import com.arrocean.dev.module.system.application.user.core.facade.AdminUserAppService
import com.arrocean.dev.module.system.application.user.core.query.GetUserByUsernameQuery
import com.arrocean.dev.module.system.application.user.core.query.UserQueryHandler
import com.arrocean.dev.module.system.constants.auth.AuthErrorCodeConstants.AUTH_FORBIDDEN
import com.arrocean.dev.module.system.constants.auth.AuthErrorCodeConstants.AUTH_PASSWORD_ERROR
import com.arrocean.dev.module.system.constants.user.UserErrorCodeConstants.USER_NOT_EXISTS
import com.arrocean.dev.module.system.domain.log.model.LoginLogDraft
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import com.arrocean.dev.module.system.domain.user.repository.AdminUserRepository
import com.arrocean.dev.module.system.enums.logger.LoginLogTypeEnum
import com.arrocean.dev.module.system.enums.logger.LoginResultEnum
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.net.InetAddress
import java.time.Clock
import java.time.Instant
import java.util.UUID

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
    private val userQueryHandler: UserQueryHandler,
    private val passwordEncoder: PasswordEncoder,
    private val loginLogService: LoginLogService,
    private val clock: Clock,
    private val userCommandHandler: UserCommandHandler,
    private val sessionTokenService: SessionTokenService,
) {

    /**
     * 管理员登录。
     */
    @Transactional
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

        return createTokenAfterLoginSuccess(user, LoginLogTypeEnum.LOGIN_PASSWORD)
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

    /**
     * 认证用户。
     *
     * @param username 用户名
     * @param password 密码
     * @return 认证成功的用户信息
     * @throws ServiceException 用户不存在、密码错误或账号被禁用时抛出相应异常
     */
    open fun authenticate(username: String, password: String): AdminUser {
        // 获取用户
        val user = userQueryHandler.handle(GetUserByUsernameQuery(username))
        if (user == null) {
//            createLoginLog(null, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionFactory.exception(USER_NOT_EXISTS)
        }
        if (!passwordEncoder.matches(password, user.passwordHash)) {
//            createLoginLog(user.id, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw ServiceExceptionFactory.exception(AUTH_PASSWORD_ERROR)
        }
        if (user.status.isDisable()) {
            // createLoginLog(user.id, username, logTypeEnum, LoginResultEnum.ACCOUNT_DISABLED);
            throw ServiceException(AUTH_FORBIDDEN)
        }

        return user
    }

    private fun createTokenAfterLoginSuccess(user: AdminUser, logType: LoginLogTypeEnum): AdminLoginResponse {
        val loginUser = toLoginUser(user)
        createLoginLog(loginUser, logType, LoginResultEnum.SUCCESS)
        updateUserLoginIp(user, WebUtils.getClientIP())
        // 生成accessToken
        val issuedTokens = sessionTokenService.issueTokens(loginUser,"default")


        return AdminLoginResponse(
            accessToken = issuedTokens.accessToken,
            tokenType = issuedTokens.tokenType,
            refreshToken = issuedTokens.refreshToken,
            expiresInSeconds = issuedTokens.accessTokenExpireSeconds,
            sessionId = issuedTokens.sessionId,
        )
    }

    private fun createLoginLog(
        user: LoginUser,
        logType: LoginLogTypeEnum,
        logResult: LoginResultEnum
    ) {
        val now = Instant.now(clock)
        val loginLog = LoginLogDraft(
            logType = logType.type,
//            traceId = traceId,
            userId = user.id,
            username = user.username,
            userType = CommonUserTypeEnum.ADMIN.value.toShort(),
            result = logResult.result,
            failReason = null,
            userIp = WebUtils.getClientIP(),
            userAgent = WebUtils.getUserAgent()?.let { if (it.length > 500) it.substring(0, 500) else it } ?: "",
//            sessionId = sessionId,
        )
        loginLogService.createLoginLog(loginLog)

    }

    private fun updateUserLoginIp(user: AdminUser, loginIp: InetAddress?) {
        if (loginIp == null) {
            return
        }
        userCommandHandler.handle(
            LoginUserCommand(
                id = user.id,
                loginIp = loginIp,
                loginTime = Instant.now(clock),
            )
        )
    }

    private fun toLoginUser(user: AdminUser): LoginUser {
        return LoginUser(
            id = user.id,
            userType = CommonUserTypeEnum.ADMIN,
            username = user.username,
            nickname = user.nickname.ifBlank { user.username },
            deptId = user.deptId,
            email = user.email,
            mobile = user.mobile,
            tenantId = user.tenantId,
            scopes = emptySet(),
        )
    }

    private fun resolveUserAgent(request: HttpRequest<*>?): String {
        return request?.headers?.get(HttpHeaders.USER_AGENT).orEmpty()
    }

    private fun resolveTraceId(request: HttpRequest<*>?): String {
        val explicitTraceId = request?.headers?.get("X-Trace-Id")?.trim()?.takeIf(String::isNotBlank)
        if (explicitTraceId != null) {
            return explicitTraceId
        }
        val traceParent = request?.headers?.get("traceparent")?.trim().orEmpty()
        val parts = traceParent.split('-')
        if (parts.size >= 4 && parts[1].isNotBlank()) {
            return parts[1]
        }
        return UUID.randomUUID().toString().replace("-", "")
    }

}


