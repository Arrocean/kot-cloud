package com.arrocean.dev.framework.common.validation

import com.arrocean.dev.framework.common.poko.PageParam
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PageSizeOrNoPageValidator : ConstraintValidator<PageSizeOrNoPage, Int?> {

    private var max: Int = PageParam.MAX_PAGE_SIZE

    override fun initialize(constraintAnnotation: PageSizeOrNoPage) {
        max = constraintAnnotation.max
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true // null 交给 @NotNull 处理
        return value == PageParam.NO_PAGE || value in 1..max
    }
}

