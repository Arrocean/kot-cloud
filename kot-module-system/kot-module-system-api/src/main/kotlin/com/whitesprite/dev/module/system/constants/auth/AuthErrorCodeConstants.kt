package com.whitesprite.dev.module.system.constants.auth

import com.whitesprite.dev.framework.common.exception.ErrorCode

/**
 * System 鉴权 错误码枚举类
 *
 * system auth，使用 1-002-000-000 段
 */
object AuthErrorCodeConstants {

    /* ========== 鉴权模块 1-002-000-000 ========== */

    // 401 - 鉴权失败
    val AUTH_PASSWORD_ERROR: ErrorCode = ErrorCode(1002000000, "密码错误", 401)

    // 403 - 禁止访问
    val AUTH_FORBIDDEN: ErrorCode = ErrorCode(1002000001, "用户被禁止访问", 403)
}