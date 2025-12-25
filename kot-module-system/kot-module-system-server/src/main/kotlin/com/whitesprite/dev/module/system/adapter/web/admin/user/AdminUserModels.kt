package com.whitesprite.dev.module.system.adapter.web.admin.user

import java.time.LocalDateTime

data class CreateUserRequest(
    val name: String,
    val password: String

)

data class UpdateUserRequest(
    val name: String
)

data class GetAdminUserResponse(
    val id: Long,
    val name: String,
    val nickname: String,
    val remark: String,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime
)