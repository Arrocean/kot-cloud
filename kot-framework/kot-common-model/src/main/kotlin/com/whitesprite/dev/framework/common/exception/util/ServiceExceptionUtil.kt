package com.whitesprite.dev.framework.common.exception.util

import org.slf4j.LoggerFactory
import com.whitesprite.dev.framework.common.exception.ErrorCode
import com.whitesprite.dev.framework.common.exception.ServiceException
import com.whitesprite.dev.framework.common.exception.constants.GlobalErrorCodeConstants

/**
 * ServiceException 工具类
 *
 * 目的在于，格式化异常信息提示。
 * 考虑到 String.format 在参数不正确时会报错，因此使用 {} 作为占位符，并使用 doFormat 方法来格式化
 *
 * @author WhiteSprite
 */
object ServiceExceptionUtil {
    private val logger = LoggerFactory.getLogger(ServiceExceptionUtil::class.java)

    // ========== 和 ServiceException 的集成 ==========

    fun exception(errorCode: ErrorCode): ServiceException {
        return exception0(errorCode.code!!, errorCode.msg!!)
    }

    fun exception(errorCode: ErrorCode, vararg params: Any?): ServiceException {
        return exception0(errorCode.code!!, errorCode.msg!!, *params)
    }

    fun exception0(code: Int, messagePattern: String, vararg params: Any?): ServiceException {
        val message = doFormat(code, messagePattern, *params)
        return ServiceException(code, message)
    }

    fun invalidParamException(messagePattern: String, vararg params: Any?): ServiceException {
        return exception0(GlobalErrorCodeConstants.BAD_REQUEST.code!!, GlobalErrorCodeConstants.BAD_REQUEST.msg!!, *params)
    }

    // ========== 格式化方法 ==========

    /**
     * 将错误编号对应的消息使用 params 进行格式化。
     *
     * @param code           错误编号
     * @param messagePattern 消息模版
     * @param params         参数
     * @return 格式化后的提示
     */
    fun doFormat(code: Int, messagePattern: String, vararg params: Any?): String {
        val sbuf = StringBuilder(messagePattern.length + 50)
        var i = 0
        var j: Int
        var l: Int

        for (l in params.indices) {
            j = messagePattern.indexOf("{}", i)
            if (j == -1) {
                logger.error("[doFormat][参数过多：错误码({})|错误内容({})|参数({})", code, messagePattern, params)
                return if (i == 0) {
                    messagePattern
                } else {
                    sbuf.append(messagePattern.substring(i)).toString()
                }
            } else {
                sbuf.append(messagePattern, i, j)
                sbuf.append(params[l])
                i = j + 2
            }
        }

        if (messagePattern.indexOf("{}", i) != -1) {
            logger.error("[doFormat][参数过少：错误码({})|错误内容({})|参数({})", code, messagePattern, params)
        }
        sbuf.append(messagePattern.substring(i))
        return sbuf.toString()
    }
}