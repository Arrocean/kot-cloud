package com.whitesprite.dev.module.system.application.user.core.command

data class DeleteUserCommand(
    val id: Long
) {
    companion object {
        fun fromId(id: Long): DeleteUserCommand {
            return DeleteUserCommand(id = id)
        }
    }
}

data class BatchDeleteUserCommand(
    val ids: List<Long>
) {
    companion object {
        fun fromIds(ids: List<Long>): BatchDeleteUserCommand {
            return BatchDeleteUserCommand(ids = ids)
        }
    }
}