package com.arrocean.dev.module.system.constants.user

import com.arrocean.dev.framework.common.exception.ErrorCode

/**
 * System 用户错误码枚举类
 *
 * system User，使用 1-002-003-000 段
 */
object UserErrorCodeConstants {

    /* ========== 用户模块 1-002-003-000 ========== */

    // 409 - 已存在/冲突
    val USER_USERNAME_EXISTS: ErrorCode = ErrorCode(1002003000, "用户账号已经存在", 409)
    val USER_MOBILE_EXISTS: ErrorCode = ErrorCode(1002003001, "手机号已经存在", 409)
    val USER_EMAIL_EXISTS: ErrorCode = ErrorCode(1002003002, "邮箱已经存在", 409)

    // 404 - 未找到
    val USER_NOT_EXISTS: ErrorCode = ErrorCode(1002003003, "用户不存在", 404)
    val USER_MOBILE_NOT_EXISTS: ErrorCode = ErrorCode(1002003010, "该手机号尚未注册", 404)
    val USER_NOT_FOUND: ErrorCode = ErrorCode(1002003012, "用户未找到", 404)

    // 400 - 请求参数问题
    val USER_IMPORT_LIST_IS_EMPTY: ErrorCode = ErrorCode(1002003004, "导入用户数据不能为空！", 400)
    val USER_IMPORT_INIT_PASSWORD: ErrorCode = ErrorCode(1002003009, "初始密码不能为空", 400)

    // 401/403 - 鉴权/禁止
    val USER_PASSWORD_FAILED: ErrorCode = ErrorCode(1002003005, "用户密码校验失败", 401)
    val USER_IS_DISABLE: ErrorCode = ErrorCode(1002003006, "名字为的用户已被禁用", 403)
    val USER_REGISTER_DISABLED: ErrorCode = ErrorCode(1002003011, "注册功能已关闭", 403)

    // 409 - 配额/状态冲突
    val USER_COUNT_MAX: ErrorCode = ErrorCode(1002003008, "创建用户失败，原因：超过租户最大租户配额({})！", 409)
}
