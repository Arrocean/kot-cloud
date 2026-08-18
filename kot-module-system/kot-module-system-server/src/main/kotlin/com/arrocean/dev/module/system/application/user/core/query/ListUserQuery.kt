package com.arrocean.dev.module.system.application.user.core.query

data class ListUserQuery(
    val keyword: String = ""
) {
    companion object {
        fun fromKeyword(keyword: String): ListUserQuery {
            return ListUserQuery(keyword = keyword)
        }
    }
}