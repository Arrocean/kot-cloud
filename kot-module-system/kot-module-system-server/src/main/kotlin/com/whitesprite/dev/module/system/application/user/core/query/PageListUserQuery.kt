package com.whitesprite.dev.module.system.application.user.core.query

class PageListUserQuery(
    val pageNo: Int,
    val pageSize: Int,
    val keyword: String = ""
) {
    companion object {
        fun fromPageNoAndPageSize(pageNo: Int, pageSize: Int, keyword: String = ""): PageListUserQuery {
            return PageListUserQuery(pageNo, pageSize, keyword)
        }
    }
}