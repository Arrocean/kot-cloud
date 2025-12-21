package com.whitesprite.dev.framework.common.enums

/**
 * 通用用户类型枚举
 *
 * @property value 值
 * @property message 描述
 * @author WhiteSprite
 */
enum class CommonUserTypeEnum(
    /**
     * 值
     */
    val value: Int,
    /**
     * 描述
     */
    val message: String
) {

    /**
     * 管理员
     */
    ADMIN(1, "管理员"),

    /**
     * 用户
     */
    USER(2, "用户");

}

/**
 * 获取用户类型枚举
 *
 * @receiver value 值
 * @return 用户类型枚举；若为 null 或非 1/2 则返回 null
 */
fun Int?.toCommonUserTypeEnum(): CommonUserTypeEnum? = when (this) {
    CommonUserTypeEnum.ADMIN.value -> CommonUserTypeEnum.ADMIN
    CommonUserTypeEnum.USER.value -> CommonUserTypeEnum.USER
    else -> null
}

/**
 * 获取用户类型枚举，若非法/空则返回默认值
 *
 * @receiver value 值
 * @param default 默认值
 * @return 用户类型枚举；若为 null 或非 1/2 则返回默认值
 */
fun Int?.toCommonUserTypeEnumOrDefault(default: CommonUserTypeEnum = CommonUserTypeEnum.USER): CommonUserTypeEnum {
    return this.toCommonUserTypeEnum() ?: default
}