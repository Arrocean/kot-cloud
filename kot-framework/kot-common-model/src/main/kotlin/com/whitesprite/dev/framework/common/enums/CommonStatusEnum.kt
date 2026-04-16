package com.whitesprite.dev.framework.common.enums

/**
 * 通用状态枚举
 *
 * @property status 状态值
 * @property bool 状态布尔值
 * @property message 状态描述
 * @author WhiteSprite
 */
enum class CommonStatusEnum(

    /**
     * 状态值
     */
    val status: Int,

    /**
     * 状态布尔值
     */
    val bool: Boolean,

    /**
     * 状态描述
     */
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
 * @return 是否启用
 */
fun Int?.isEnable(): Boolean {
    return this.toCommonStatusEnum()?.bool == true
}

/**
 * 判断是否启用
 * 针对Short类型的状态值
 *
 * @return 是否启用
 */
fun Short?.isEnable(): Boolean {
    return this?.toInt().toCommonStatusEnum()?.bool == true
}

/**
 * 判断是否禁用
 *
 * @return 是否禁用
 */
fun Int?.isDisable(): Boolean {
    return this.toCommonStatusEnum()?.bool == false
}

/**
 * 判断是否禁用
 * 针对Short类型的状态值
 *
 * @return 是否禁用
 */
fun Short?.isDisable(): Boolean {
    return this?.toInt().toCommonStatusEnum()?.bool == false
}

/**
 * 将数据库状态值(1/0)转换为 [CommonStatusEnum]
 *
 * @receiver status 状态值
 * @return 对应枚举；若为 null 或非 0/1 则返回 null
 */
fun Int?.toCommonStatusEnum(): CommonStatusEnum? = when (this) {
    CommonStatusEnum.ENABLE.status -> CommonStatusEnum.ENABLE
    CommonStatusEnum.DISABLE.status -> CommonStatusEnum.DISABLE
    else -> null
}

/**
 * 将布尔值(true/false)转换为 [CommonStatusEnum]
 *
 * @receiver bool 布尔值
 * @return 对应枚举；若为 null 则返回 null
 */
fun Boolean?.toCommonStatusEnum(): CommonStatusEnum? = when (this) {
    true -> CommonStatusEnum.ENABLE
    false -> CommonStatusEnum.DISABLE
    null -> null
}

/**
 * 将数据库状态值(1/0)转换为 [CommonStatusEnum]，若非法/空则返回默认值
 *
 * @receiver status 状态值
 * @param default 默认值
 * @return 对应枚举；若为 null 或非 0/1 则返回默认值
 */
fun Int?.toCommonStatusEnumOrDefault(default: CommonStatusEnum = CommonStatusEnum.DISABLE): CommonStatusEnum {
    return this.toCommonStatusEnum() ?: default
}