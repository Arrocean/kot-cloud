package com.arrocean.dev.module.member.adapter.web.member.user

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.module.member.application.user.MemberUserService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(ApiPrefix.API_V1 + "/user")
open class MemberUserControllerV1(
    private val memberUserService: MemberUserService,
) {
    @Get("/me")
    open fun getCurrentUser(): CommonResult<MemberUserProfileResponse> =
        success(memberUserService.getCurrentProfile())

    @Put("/me")
    open fun updateCurrentUser(@Body @Valid req: UpdateMemberUserProfileRequest): CommonResult<MemberUserProfileResponse> =
        success(memberUserService.updateCurrentProfile(req))
}
