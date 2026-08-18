package com.arrocean.dev.module.system.application.user.core.query

import com.arrocean.dev.framework.common.poko.PageParam

class PageListUserQuery(
    val page: PageParam,
    val keyword: String?
) {
    companion object {
        fun fromPage(page: PageParam, keyword: String?): PageListUserQuery {
            return PageListUserQuery(page, keyword)
        }
    }
}
