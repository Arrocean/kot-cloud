package com.whitesprite.dev.framework.core

import com.whitesprite.dev.framework.common.poko.PageResult
import io.micronaut.data.model.Page

/**
 * Page 转 PageResult
 */
fun <T> Page<T>.toPageResult(): PageResult<T> =
    PageResult(total = totalSize, list = content)

fun <T, R> Page<T>.toPageResult(mapper: (T) -> R): PageResult<R> =
    PageResult(total = totalSize, list = content.map(mapper))

fun <T> List<T>.toPageResultTotalEqualsSize(): PageResult<T> =
    PageResult(total = size.toLong(), list = this)