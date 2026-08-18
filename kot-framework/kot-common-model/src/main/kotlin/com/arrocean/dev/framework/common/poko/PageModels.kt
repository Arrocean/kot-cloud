package com.arrocean.dev.framework.common.poko

import com.arrocean.dev.framework.common.validation.PageSizeOrNoPage
import io.micronaut.data.model.Pageable
import io.micronaut.serde.annotation.Serdeable
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
    fun isNoPage(): Boolean = pageSize == NO_PAGE

    fun toPageable(): Pageable {
        require(pageNo >= DEFAULT_PAGE_NO) { "pageNo 必须大于等于 $DEFAULT_PAGE_NO" }
        require(isNoPage() || pageSize in DEFAULT_PAGE_NO..MAX_PAGE_SIZE) {
            "pageSize 必须是 $NO_PAGE（不分页）或在 $DEFAULT_PAGE_NO 到 $MAX_PAGE_SIZE 之间"
        }

        return if (isNoPage()) {
            Pageable.unpaged()
        } else {
            Pageable.from(pageNo - 1, pageSize)
        }
    }

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

@Serdeable
data class PageResult<T>(
    val total: Long,
    val list: List<T>
) {
    constructor(total: Long) : this(total, emptyList())

    companion object {
        fun <T> empty(): PageResult<T> {
            return PageResult(0, emptyList())
        }

        fun <T> empty(total: Long): PageResult<T> {
            return PageResult(total)
        }
    }
}
