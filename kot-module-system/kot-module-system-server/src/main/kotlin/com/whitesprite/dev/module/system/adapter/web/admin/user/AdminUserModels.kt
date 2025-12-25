package com.whitesprite.dev.module.system.adapter.web.admin.user

import java.time.LocalDateTime

/**
 * 创建用户请求
 *
 * @author WhiteSprite
 */
data class CreateUserRequest(
    val name: String,
    val password: String

)

/**
 * 更新用户请求
 */
data class UpdateUserRequest(
    val name: String
)

/**
 * 获取用户响应
 */
data class GetAdminUserResponse(
    val id: Long,
    val name: String,
    val nickname: String,
    val remark: String,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime
)