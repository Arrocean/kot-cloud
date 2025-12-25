package com.whitesprite.dev.module.system.application.user.command

import com.whitesprite.dev.module.system.adapter.web.admin.user.UpdateUserRequest

data class UpdateUserCommand(
    val id: Long,
    val name: String
) {
    companion object {
        fun fromRequest(id: Long, request: UpdateUserRequest): UpdateUserCommand {
            return UpdateUserCommand(
                id = id,
                name = request.name
            )
        }
    }
}