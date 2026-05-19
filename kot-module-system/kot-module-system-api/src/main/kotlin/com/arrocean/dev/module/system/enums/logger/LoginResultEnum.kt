package com.arrocean.dev.module.system.enums.logger

/**
 * 登录结果枚举。
 */
enum class LoginResultEnum(
    val result: Int,
) {
    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 表示登录尝试中提供的凭证无效。这通常意味着用户名与密码的组合不正确。
     */
    BAD_CREDENTIALS(10),

    /**
     * 表示用户账号已被禁用。尝试使用被禁用的账号进行登录时返回此状态。
     */
    USER_DISABLED(20),

    /**
     * 表示登录过程中所需的验证码未找到。这通常意味着用户在尝试进行需要验证码验证的登录时，未能提供有效的验证码。
     */
    CAPTCHA_NOT_FOUND(30),

    /**
     * 表示登录过程中提供的验证码错误。这通常意味着用户输入的验证码与系统生成的不匹配。
     */
    CAPTCHA_CODE_ERROR(31)
}

