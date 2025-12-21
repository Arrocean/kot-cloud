package com.whitesprite.dev.module.system.controller.admin.user.vo

data class CreateUserRequestVO(
    val name: String,
    val password: String

)

data class UpdateUserRequestVO(
    val name: String
)