package com.arrocean.dev.module.system.application.user.core.command

import com.arrocean.dev.module.system.adapter.web.admin.user.CreateUserRequest

data class CreateUserCommand(
    val name: String,
    val password: String
) {
    companion object {
        fun fromRequest(request: CreateUserRequest): CreateUserCommand {
            return CreateUserCommand(
                name = request.username,
                password = request.password
            )
        }
    }
}

data class RegisterUserCommand(
    val username: String,
    val password: String,
    val nickname: String,
)
