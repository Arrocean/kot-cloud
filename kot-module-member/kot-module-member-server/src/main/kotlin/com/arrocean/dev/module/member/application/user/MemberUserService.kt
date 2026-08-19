package com.arrocean.dev.module.member.application.user

import com.arrocean.dev.framework.common.enums.CommonUserTypeEnum
import com.arrocean.dev.framework.common.enums.isDisable
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.common.util.web.WebUtils
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.framework.security.core.context.LoginUser
import com.arrocean.dev.framework.security.core.password.PasswordEncoder
import com.arrocean.dev.framework.security.core.token.SessionTokenService
import com.arrocean.dev.module.member.adapter.web.member.auth.MemberLoginRequest
import com.arrocean.dev.module.member.adapter.web.member.auth.MemberLoginResponse
import com.arrocean.dev.module.member.adapter.web.member.auth.MemberRegisterByMobileRequest
import com.arrocean.dev.module.member.adapter.web.member.auth.MemberRegisterByUsernameRequest
import com.arrocean.dev.module.member.adapter.web.member.user.MemberUserProfileResponse
import com.arrocean.dev.module.member.adapter.web.member.user.UpdateMemberUserProfileRequest
import com.arrocean.dev.module.member.infrastructure.persistence.entity.user.MemberUserEntity
import com.arrocean.dev.module.member.infrastructure.persistence.postgresql.user.MemberUserEntityRepository
import io.micronaut.http.HttpHeaders
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.time.Clock
import java.time.Instant

@Singleton
open class MemberUserService(
    private val memberUserRepository: MemberUserEntityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sessionTokenService: SessionTokenService,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
    private val clock: Clock,
) {
    @Transactional
    open fun login(req: MemberLoginRequest): MemberLoginResponse {
        val account = req.usernameOrMobile.trim()
        val user = memberUserRepository.findByUsernameAndDeletedFalse(account).orElse(null)
            ?: memberUserRepository.findByMobileAndDeletedFalse(account).orElse(null)
            ?: throw ServiceExceptionFactory.unauthorized("用户名、手机号或密码错误")
        if (!passwordEncoder.matches(req.password, user.passwordHash)) {
            throw ServiceExceptionFactory.unauthorized("用户名、手机号或密码错误")
        }
        if (user.status.isDisable()) {
            throw ServiceExceptionFactory.forbidden("账号已被禁用")
        }

        user.loginIp = WebUtils.getClientIP()
        user.loginTime = Instant.now(clock)
        memberUserRepository.update(user)
        val issuedTokens = sessionTokenService.issueTokens(toLoginUser(user), "member")
        return MemberLoginResponse(
            accessToken = issuedTokens.accessToken,
            tokenType = issuedTokens.tokenType,
            refreshToken = issuedTokens.refreshToken,
            expiresInSeconds = issuedTokens.accessTokenExpireSeconds,
            sessionId = issuedTokens.sessionId,
        )
    }

    open fun registerByUsername(@Suppress("UNUSED_PARAMETER") req: MemberRegisterByUsernameRequest): Nothing {
        throw ServiceExceptionFactory.notImplemented("会员注册暂未开放")
    }

    open fun registerByMobile(@Suppress("UNUSED_PARAMETER") req: MemberRegisterByMobileRequest): Nothing {
        throw ServiceExceptionFactory.notImplemented("会员注册暂未开放")
    }

    open fun logout() {
        val authorization = WebUtils.getRequest()?.headers?.get(HttpHeaders.AUTHORIZATION).orEmpty()
        val accessToken = authorization.removePrefix("Bearer ").trim()
        if (accessToken.isNotBlank()) {
            sessionTokenService.revokeByAccessToken(accessToken)
        }
    }

    open fun getCurrentProfile(): MemberUserProfileResponse = toProfile(findCurrentUser())

    @Transactional
    open fun updateCurrentProfile(req: UpdateMemberUserProfileRequest): MemberUserProfileResponse {
        val user = findCurrentUser()
        req.nickname?.trim()?.let { user.nickname = it }
        req.email?.trim()?.let { user.email = it.ifBlank { null } }
        req.avatarUrl?.trim()?.let { user.avatarUrl = it.ifBlank { null } }
        return toProfile(memberUserRepository.update(user))
    }

    private fun findCurrentUser(): MemberUserEntity {
        val loginUser = currentLoginUserProvider.requireLoginUser()
        if (loginUser.userType != CommonUserTypeEnum.USER) {
            throw ServiceExceptionFactory.forbidden("当前登录用户不是会员")
        }
        return memberUserRepository.findByIdAndDeletedFalse(loginUser.id).orElseThrow {
            ServiceExceptionFactory.unauthorized("会员账号不存在或已删除")
        }
    }

    private fun toLoginUser(user: MemberUserEntity): LoginUser = LoginUser(
        id = requireNotNull(user.id) { "会员用户 ID 不能为空" },
        userType = CommonUserTypeEnum.USER,
        username = user.username,
        nickname = user.nickname.ifBlank { user.username },
        email = user.email,
        mobile = user.mobile,
        tenantId = 0L,
    )

    private fun toProfile(user: MemberUserEntity): MemberUserProfileResponse = MemberUserProfileResponse(
        id = requireNotNull(user.id) { "会员用户 ID 不能为空" },
        username = user.username,
        mobile = user.mobile,
        nickname = user.nickname,
        email = user.email,
        avatarUrl = user.avatarUrl,
    )
}
