package com.whitesprite.dev.framework.common.enums

enum class CommonStatusEnum(
    val status: Int,
    val bool: Boolean,
    val message: String
) {

    /**
     * 启用
     */
    ENABLE(1, true, "启用"),

    /**
     * 禁用
     */
    DISABLE(0, false, "禁用");

}

/**
 * 判断是否启用
 *
 * @param status 状态
 * @return 是否启用
 */
fun isEnable(status: Int?): Boolean {
    return status == CommonStatusEnum.ENABLE.status
}

/**
 * 判断是否禁用
 *
 * @param status 状态
 * @return 是否禁用
 */
fun isDisable(status: Int?): Boolean {
    return status == CommonStatusEnum.DISABLE.status
}
