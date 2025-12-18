package com.whitesprite.dev.framework.common.poko

import com.whitesprite.dev.framework.common.validation.PageSizeOrNoPage
import jakarta.validation.Valid
import jakarta.validation.constraints.Min

/**
 * 分页参数
 * @property pageNo 页码
 * @property pageSize 页大小
 * @author WhiteSprite
 */
data class PageParam(
    @field:Min(value = 1L)
    val pageNo: Int = DEFAULT_PAGE_NO,

    @field:Min(value = 1L)
    @field:PageSizeOrNoPage(max = MAX_PAGE_SIZE)
    val pageSize: Int = DEFAULT_PAGE_SIZE
) {
    companion object {
        /**
         * 默认页码
         */
        const val DEFAULT_PAGE_NO = 1

        /**
         * 默认页大小
         */
        const val DEFAULT_PAGE_SIZE = 10

        /**
         * 最大页大小
         */
        const val MAX_PAGE_SIZE = 200

        /**
         * 每页条数 不分页值
         */
        const val NO_PAGE = -1
    }
}

data class SortingField(
    val field: String,
    val direction: SortDirection = SortDirection.ASC
)

enum class SortDirection {
    ASC,
    DESC
}

/**
 * 支持排序的分页参数
 */
data class SortablePageParam(
    @field:Valid
    val page: PageParam = PageParam(),

    val sorting: List<SortingField> = emptyList()
) {
    /** 便捷访问（不参与序列化，必要时可 @JsonIgnore） */
    val pageNo: Int get() = page.pageNo
    val pageSize: Int get() = page.pageSize
}

data class PageResult<T>(
    val total: Long,
    val records: List<T>
)
