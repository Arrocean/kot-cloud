package com.whitesprite.dev.module.system.application.user.command

import com.whitesprite.dev.module.system.adapter.web.admin.user.CreateUserRequest

data class CreateUserCommand(
    val name: String,
    val password: String
) {
    companion object {
        fun fromRequest(request: CreateUserRequest): CreateUserCommand {
            return CreateUserCommand(
                name = request.name,
                password = request.password
            )
        }
    }
}