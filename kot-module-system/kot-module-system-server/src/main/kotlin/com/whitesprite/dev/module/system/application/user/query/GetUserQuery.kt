package com.whitesprite.dev.module.system.application.user.query

data class GetUserQuery(
    val id: Long
) {
    companion object {
        fun fromId(id: Long): GetUserQuery {
            return GetUserQuery(id = id)
        }
    }
}