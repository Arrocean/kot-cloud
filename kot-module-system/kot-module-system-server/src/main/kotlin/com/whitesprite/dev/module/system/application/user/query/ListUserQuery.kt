package com.whitesprite.dev.module.system.application.user.query

data class ListUserQuery(
    val keyword: String = ""
) {
    companion object {
        fun fromKeyword(keyword: String): ListUserQuery {
            return ListUserQuery(keyword = keyword)
        }
    }
}