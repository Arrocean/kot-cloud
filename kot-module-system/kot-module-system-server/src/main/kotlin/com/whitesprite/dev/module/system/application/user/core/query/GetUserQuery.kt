package com.whitesprite.dev.module.system.application.user.core.query

data class GetUserQuery(
    val id: Long,
    val username: String? = null
) {
    companion object {
        fun fromId(id: Long): GetUserQuery {
            return GetUserQuery(id = id)
        }
    }
}