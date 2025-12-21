package com.whitesprite.dev.framework.common.validation

import com.whitesprite.dev.framework.common.poko.PageParam
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
class PageSizeOrNoPageValidator :
    ConstraintValidator<PageSizeOrNoPage, Int> {

    override fun isValid(
        value: Int?,
        annotationMetadata: AnnotationValue<PageSizeOrNoPage>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            // null 交给 @NotNull 决定
            return true
        }

        val max = annotationMetadata.get("max", Int::class.java)
            .orElse(PageParam.MAX_PAGE_SIZE)

        return value == PageParam.NO_PAGE || (value in 1..max)
    }
}