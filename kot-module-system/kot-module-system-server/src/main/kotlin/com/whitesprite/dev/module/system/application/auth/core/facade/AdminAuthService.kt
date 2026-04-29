package com.whitesprite.dev.module.system.application.auth.core.facade

import com.whitesprite.dev.framework.common.enums.CommonStatusEnum
import com.whitesprite.dev.framework.common.enums.CommonUserTypeEnum
import com.whitesprite.dev.framework.common.enums.isDisable
import com.whitesprite.dev.framework.common.exception.ServiceException
import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.security.core.context.CurrentLoginUserProvider
import com.whitesprite.dev.framework.security.core.context.LoginUser
import com.whitesprite.dev.framework.security.core.password.PasswordEncoder
import com.whitesprite.dev.framework.security.core.token.TokenService
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthAssembler
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminAuthProfileResponse
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginRequest
import com.whitesprite.dev.module.system.adapter.web.admin.auth.AdminLoginResponse
import com.whitesprite.dev.module.system.application.user.core.query.GetUserByUsernameQuery
import com.whitesprite.dev.module.system.application.user.core.query.UserQueryHandler
import com.whitesprite.dev.module.system.constants.auth.AuthErrorCodeConstants.AUTH_FORBIDDEN
import com.whitesprite.dev.module.system.constants.auth.AuthErrorCodeConstants.AUTH_PASSWORD_ERROR
import com.whitesprite.dev.module.system.constants.user.UserErrorCodeConstants.USER_NOT_EXISTS
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.enums.logger.LoginLogTypeEnum
import jakarta.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    private val tokenService: TokenService,
) {

    /**
     * 处理管理员登录请求。
     *
     * @param req [AdminLoginRequest] 对象，包含用户名、密码等信息
     * @return [AdminLoginResponse] 对象，包含访问令牌、令牌类型以及会话ID等信息
     */
    open suspend fun login(req: AdminLoginRequest): AdminLoginResponse {
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
        return createTokenAfterLoginSuccess(user, req)

//        throw ServiceExceptionFactory.notImplemented(
//            "管理员登录链路待实现：后续补充用户校验、密码比对于 Token 签发"
//        )
    }

    /**
     * 使当前登录的管理员用户登出。
     *
     * 当前实现为抛出异常，提示该功能尚未完成。未来的实现中，计划引入Redis或会话持久化机制，以及Token黑名单机制来实际处理用户的登出逻辑，包括但不限于撤销会话、使访问令牌失效等操作。
     *
     * @throws ServiceException 指示登出功能未实现
     */
    open fun logout() {
        // TODO WhiteSprite：当引入 Redis / 会话持久化或 Token 黑名单后，在这里真正注销登录态
        throw ServiceExceptionFactory.notImplemented(
            "管理员登出链路待实现：后续补充会话撤销或 Token 失效处理"
        )
    }

    /**
     * 获取当前登录用户的详细信息。
     *
     * 该方法首先通过[CurrentLoginUserProvider.requireLoginUser]获取当前已登录的用户，然后使用[AdminAuthAssembler.toProfileResponse]将登录用户的信息转换为[AdminAuthProfileResponse]对象返回。如果当前没有用户登录，则会抛出异常。
     *
     * @return [AdminAuthProfileResponse] 包含了当前登录用户的ID、用户名、昵称、部门ID、邮箱、手机号、租户ID、授权范围以及会话编号等信息。
     */
    open fun getProfile(): AdminAuthProfileResponse {
        val loginUser = currentLoginUserProvider.requireLoginUser()
        return AdminAuthAssembler.toProfileResponse(loginUser)
    }

    /**
     * 验证给定的用户名和密码，返回验证通过的用户。
     *
     * @param username 用户名
     * @param password 密码
     * @return [AdminUser] 对象，表示验证通过的用户
     * @throws ServiceException 如果用户名不存在、密码错误或账户被禁用，则抛出服务异常
     */
    open suspend fun authenticate(username: String, password: String): AdminUser {
        val logTypeEnum = LoginLogTypeEnum.LOGIN_PASSWORD
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

//    private fun createLoginLog(
//        userId: Long?, username: String?,
//        logTypeEnum: LoginLogTypeEnum, loginResult: LoginResultEnum
//    ) {
//        // 插入登录日志
//        val reqDTO: LoginLogCreateReqDTO = LoginLogCreateReqDTO()
//        reqDTO.setLogType(logTypeEnum.getType())
//        reqDTO.setTraceId(TracerUtils.getTraceId())
//        reqDTO.setUserId(userId)
//        reqDTO.setUserType(getUserType().getValue())
//        reqDTO.setUsername(username)
//        reqDTO.setUserAgent(ServletUtils.getUserAgent())
//        reqDTO.setUserIp(ServletUtils.getClientIP())
//        reqDTO.setResult(loginResult.getResult())
//        loginLogService.createLoginLog(reqDTO)
//        // 更新最后登录时间
//        if (userId != null && LoginResultEnum.SUCCESS.getResult() == loginResult.getResult()) {
//            userService.updateUserLogin(userId, ServletUtils.getClientIP())
//        }
//    }

    /**
     * 在管理员登录成功后创建访问令牌。
     *
     * @param user 登录成功的[AdminUser]对象
     * @param req [AdminLoginRequest]请求对象，包含用户名和密码等信息
     * @return [AdminLoginResponse]对象，包含生成的访问令牌、令牌类型、过期时间（秒）以及会话ID
     */
    @OptIn(ExperimentalUuidApi::class)
    private suspend fun createTokenAfterLoginSuccess(user: AdminUser, req: AdminLoginRequest): AdminLoginResponse{
        // 插入登录日志
        // TODO createLoginLog(user.id, req.username, logTypeEnum, loginResult.SUCCESS)

        // 生成会话 ID
        val sessionId = Uuid.random().toString()

        // 解析 scopes
        // TODO WhiteSprite：后续补充用户权限查询逻辑，目前先写死一个 admin 权限
        val scopes = setOf("admin")

        // 构造 LoginUser 对象
        val loginUser = LoginUser(
            id = user.id,
            userType = CommonUserTypeEnum.ADMIN,
            username = user.username,
            nickname = user.nickname,
            deptId = user.deptId,
            email = user.email,
            mobile = user.mobile,
            tenantId = user.tenantId,
            scopes = scopes,
            sessionId = sessionId
        )

        val accessToken = tokenService.generateAccessToken(loginUser)

        return AdminLoginResponse(
            accessToken = accessToken,
            tokenType = "Bearer",
            expiresInSeconds = 86400,
            sessionId = sessionId
        )
    }
}

