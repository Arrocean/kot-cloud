package com.arrocean.dev.module.member.adapter.web.member.auth

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.module.member.application.user.MemberUserService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(ApiPrefix.API_V1 + "/auth")
open class MemberAuthControllerV1(
    private val memberUserService: MemberUserService,
) {
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/login")
    open fun login(@Body @Valid req: MemberLoginRequest): CommonResult<MemberLoginResponse> =
        success(memberUserService.login(req))

    @Post("/logout")
    open fun logout(): CommonResult<Boolean> {
        memberUserService.logout()
        return success(true)
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/register-by-username")
    open fun registerByUsername(@Body @Valid req: MemberRegisterByUsernameRequest): CommonResult<Nothing> =
        success(memberUserService.registerByUsername(req))

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/register-by-mobile")
    open fun registerByMobile(@Body @Valid req: MemberRegisterByMobileRequest): CommonResult<Nothing> =
        success(memberUserService.registerByMobile(req))
}
