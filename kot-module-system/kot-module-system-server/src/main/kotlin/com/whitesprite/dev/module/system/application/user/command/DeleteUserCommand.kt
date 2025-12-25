package com.whitesprite.dev.module.system.application.user.command

data class DeleteUserCommand(
    val id: Long
) {
    companion object {
        fun fromId(id: Long): DeleteUserCommand {
            return DeleteUserCommand(id = id)
        }
    }
}