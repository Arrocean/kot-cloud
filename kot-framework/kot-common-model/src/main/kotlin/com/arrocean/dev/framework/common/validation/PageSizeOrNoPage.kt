package com.arrocean.dev.framework.common.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * 分页参数校验注解
 *
 * @author WhiteSprite
 */
@MustBeDocumented
@Constraint(validatedBy = [PageSizeOrNoPageValidator::class])
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class PageSizeOrNoPage(
    /**
     * 错误信息
     */
    val message: String = "pageSize 必须是 -1 (无分页) 或在 1 到 {max} 之间",
    /**
     * 最大值
     */
    val max: Int,
    /**
     * 分组
     */
    val groups: Array<KClass<*>> = [],
    /**
     * 负载
     */
    val payload: Array<KClass<out Payload>> = []
)