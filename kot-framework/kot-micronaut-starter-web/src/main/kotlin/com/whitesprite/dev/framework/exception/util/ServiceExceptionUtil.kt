package com.whitesprite.dev.framework.exception.util

import com.whitesprite.dev.framework.common.exception.ErrorCode
import com.whitesprite.dev.framework.common.exception.ServiceException
import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import org.slf4j.LoggerFactory

/**
 * ServiceException 工具类
 *
 * 目的在于，格式化异常信息提示。
 */
object ServiceExceptionUtil {

    private val log = LoggerFactory.getLogger(ServiceExceptionUtil::class.java)

    fun exception(errorCode: ErrorCode): ServiceException {
        val ex = ServiceExceptionFactory.exception(errorCode)
        logCreate(errorCode, ex)
        return ex
    }

    fun exception(errorCode: ErrorCode, vararg params: Any?): ServiceException {
        val ex = ServiceExceptionFactory.exception(errorCode, *params)
        logCreate(errorCode, ex)
        return ex
    }

    fun exception(code: Int, msg: String, httpStatusCode: Int = 500): ServiceException {
        val ex = ServiceExceptionFactory.exception(code, msg, httpStatusCode)
        log.info("创建业务异常:: httpStatusCode={}, code={}, msg={}", httpStatusCode, code, msg)
        return ex
    }

    private fun logCreate(errorCode: ErrorCode, ex: ServiceException) {

        log.info(
            "创建业务异常: httpStatusCode={}, code={}, msg={}",
            errorCode.httpStatusCode,
            errorCode.code,
            ex.message ?: errorCode.msg
        )
    }
}