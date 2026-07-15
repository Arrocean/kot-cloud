package com.arrocean.dev.module.system.enums.logger

/**
 * 登录日志类型枚举
 *
 * @param type 日志类型
 * 100~199 => 登录相关；
 * 200~299 => 登出相关；
 */
enum class LoginLogTypeEnum(
    /**
     * 日志类型
     * 100~199 => 登录相关；
     * 200~299 => 登出相关；
     */
    val type: Int
) {
    /**
     * 账号密码登录
     */
    LOGIN_PASSWORD(100),

    /**
     * 社交登录
     * PS：后续补充社交登录相关类型，如 LOGIN_SOCIAL_WECHAT、LOGIN_SOCIAL_ALIPAY 等
     */
    LOGIN_SOCIAL(101),

    /**
     * 手机号密码登录
     */
    LOGIN_PHONE_PASSWORD(102),

    /**
     * 手机号验证码登录
     */
    LOGIN_PHONE_CODE(103),

    /**
     * 主动登出
     */
    LOGOUT_ACTIVE(201),

    /**
     * 被动登出（如被踢下线）
     */
    LOGOUT_PASSIVE(202);
}

