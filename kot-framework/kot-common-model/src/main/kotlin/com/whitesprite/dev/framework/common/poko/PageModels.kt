package com.whitesprite.dev.framework.common.poko

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PageParam(
    // TODO WhiteSprite：需要考虑怎么取消魔法值；
    @field:Min(value = 1) val pageNo: Int = 1,
    // TODO WhiteSprite：需要考虑怎么取消魔法值；需要考虑为-1情况下的查询所有数据处理
    @field:Min(value = 1) @field:Max(value = 200) val pageSize: Int = 10
)

data class SortingField(
    val field: String,
    val direction: SortDirection = SortDirection.ASC
)

enum class SortDirection {
    ASC,
    DESC
}

data class SortablePageParam(
    val page: Int = 1,
    val size: Int = 10,
    val sorting: List<SortingField> = emptyList()
)

data class PageResult<T>(
    val page: Int,
    val size: Int,
    val total: Long,
    val records: List<T>
)
