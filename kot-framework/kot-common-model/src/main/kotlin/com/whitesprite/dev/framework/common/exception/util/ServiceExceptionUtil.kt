package com.whitesprite.dev.framework.common.exception.util

import com.whitesprite.dev.framework.common.exception.ErrorCode
import com.whitesprite.dev.framework.common.exception.ServiceException
import java.util.*

/**
 * ServiceException 工具类
 *
 * 目的在于，格式化异常信息提示。
 * 考虑到 String.format 在参数不正确时会报错，因此使用 {} 作为占位符，并使用 doFormat 方法来格式化
 *
 * @author WhiteSprite
 */
object ServiceExceptionFactory {

    fun exception(errorCode: ErrorCode): ServiceException {
        val code = requireNotNull(errorCode.code) { "ErrorCode.code 不能为空" }
        val msg = requireNotNull(errorCode.msg) { "ErrorCode.msg 不能为空" }
        // 保留原有 httpStatusCode
        return ServiceException(ErrorCode(code, msg, errorCode.httpStatusCode))
    }

    fun exception(errorCode: ErrorCode, vararg params: Any?): ServiceException {
        val code = requireNotNull(errorCode.code) { "ErrorCode.code 不能为空" }
        val pattern = requireNotNull(errorCode.msg) { "ErrorCode.msg 不能为空" }
        val msg = safeFormat(pattern, params)
        return ServiceException(ErrorCode(code, msg, errorCode.httpStatusCode))
    }

    fun exception(code: Int, msg: String, httpStatusCode: Int = 500): ServiceException {
        return ServiceException(ErrorCode(code, msg, httpStatusCode))
    }

    private fun safeFormat(pattern: String, params: Array<out Any?>): String {
        if (params.isEmpty()) return pattern
        return try {
            String.format(pattern, *params)
        } catch (_: IllegalFormatException) {
            // 不在这里打日志：保持纯
            pattern
        }
    }
}