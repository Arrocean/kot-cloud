package com.arrocean.dev.framework.core

import com.arrocean.dev.framework.common.poko.PageResult
import io.micronaut.data.model.Page

/**
 * Page 转 PageResult
 */
fun <T : Any> Page<T>.toPageResult(): PageResult<T> =
    PageResult(total = totalSize, list = content)

fun <T : Any, R> Page<T>.toPageResult(mapper: (T) -> R): PageResult<R> =
    PageResult(total = totalSize, list = content.map(mapper))

fun <T> List<T>.toPageResultTotalEqualsSize(): PageResult<T> =
    PageResult(total = size.toLong(), list = this)
