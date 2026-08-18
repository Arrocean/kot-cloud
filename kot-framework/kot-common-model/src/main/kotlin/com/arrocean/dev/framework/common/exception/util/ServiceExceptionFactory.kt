package com.arrocean.dev.framework.common.exception.util

import com.arrocean.dev.framework.common.exception.ErrorCode
import com.arrocean.dev.framework.common.exception.ServiceException
import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants

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
        return ServiceException(errorCode)
    }

    fun exception(errorCode: ErrorCode, vararg params: Any?): ServiceException {
        val msg = ErrorMessageFormatter.format(errorCode.code, errorCode.msg, *params)
        return ServiceException(ErrorCode(errorCode.code, msg, errorCode.httpStatusCode))
    }

    fun exception(code: Int, msg: String, httpStatusCode: Int = 500): ServiceException {
        return ServiceException(ErrorCode(code, msg, httpStatusCode))
    }

    fun badRequest(messagePattern: String = GlobalErrorCodeConstants.BAD_REQUEST.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.BAD_REQUEST, messagePattern, *params)
    }

    fun unauthorized(messagePattern: String = GlobalErrorCodeConstants.UNAUTHORIZED.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.UNAUTHORIZED, messagePattern, *params)
    }

    fun forbidden(messagePattern: String = GlobalErrorCodeConstants.FORBIDDEN.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.FORBIDDEN, messagePattern, *params)
    }

    fun notImplemented(messagePattern: String = GlobalErrorCodeConstants.NOT_IMPLEMENTED.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.NOT_IMPLEMENTED, messagePattern, *params)
    }

    fun configurationError(messagePattern: String = GlobalErrorCodeConstants.ERROR_CONFIGURATION.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.ERROR_CONFIGURATION, messagePattern, *params)
    }

    fun internalServerError(messagePattern: String = GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.msg, vararg params: Any?): ServiceException {
        return global(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR, messagePattern, *params)
    }

    private fun global(errorCode: ErrorCode, messagePattern: String, vararg params: Any?): ServiceException {
        return if (messagePattern == errorCode.msg && params.isEmpty()) {
            exception(errorCode)
        } else {
            exception(errorCode.code, ErrorMessageFormatter.format(errorCode.code, messagePattern, *params), errorCode.httpStatusCode)
        }
    }
}