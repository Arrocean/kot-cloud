package com.whitesprite.dev.framework.common.poko

data class PageParam(
    val page: Int = 1,
    val size: Int = 10
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
