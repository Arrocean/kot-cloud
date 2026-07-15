package com.arrocean.dev.framework.common.exception.constants

import com.arrocean.dev.framework.common.exception.ErrorCode

/**
 * 全局错误码常量
 * 0-999 系统异常编码保留
 *
 * 一般情况下，使用 HTTP 响应状态码 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * 成功为0，较为特殊
 *
 * @author WhiteSprite
 */
object GlobalErrorCodeConstants {
    /**
     * 成功
     */
    val SUCCESS = ErrorCode(0, "成功")

    /* ========== 客户端错误段 ========== */

    val BAD_REQUEST = ErrorCode(400, "请求参数不正确")
    val UNAUTHORIZED = ErrorCode(401, "账号未登录")
    val FORBIDDEN = ErrorCode(403, "账号禁止访问")
    val NOT_FOUND = ErrorCode(404, "请求资源不存在")
    val METHOD_NOT_ALLOWED = ErrorCode(405, "请求方法不允许")
    /**
     * 并发请求导致触发锁
     */
    val LOCKED = ErrorCode(423, "请求失败，请稍后重试")
    val TOO_MANY_REQUESTS = ErrorCode(429, "请求过于频繁")

    /* ========== 服务端错误段 ========== */

    val INTERNAL_SERVER_ERROR = ErrorCode(500, "系统异常")
    val NOT_IMPLEMENTED = ErrorCode(501, "功能未实现/开启")
    val ERROR_CONFIGURATION = ErrorCode(502, "系统配置错误")

    /* ========== 自定义错误段 ========== */

    val REPEATED_REQUEST = ErrorCode(900, "请勿重复请求")
    val DEMO_DENY = ErrorCode(901, "演示模式，禁止操作")

    val UNKNOWN = ErrorCode(999, "未知错误")

}
