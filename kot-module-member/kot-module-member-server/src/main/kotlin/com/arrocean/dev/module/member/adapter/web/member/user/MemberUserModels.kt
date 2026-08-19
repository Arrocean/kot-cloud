package com.arrocean.dev.module.member.adapter.web.member.user

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

@Serdeable
data class MemberUserProfileResponse(
    val id: Long,
    val username: String,
    val mobile: String? = null,
    val nickname: String,
    val email: String? = null,
    val avatarUrl: String? = null,
)

@Serdeable
data class UpdateMemberUserProfileRequest(
    @field:Size(min = 1, max = 128, message = "昵称长度必须在 1 到 128 位之间")
    val nickname: String? = null,
    @field:Email(message = "邮箱格式不正确")
    @field:Size(max = 64, message = "邮箱长度不能超过 64 位")
    val email: String? = null,
    @field:Size(max = 1024, message = "头像地址长度不能超过 1024 位")
    val avatarUrl: String? = null,
)
