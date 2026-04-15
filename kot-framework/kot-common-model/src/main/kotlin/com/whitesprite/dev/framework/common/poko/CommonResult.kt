package com.whitesprite.dev.framework.common.poko

import com.whitesprite.dev.framework.common.exception.ErrorCode
import com.whitesprite.dev.framework.common.exception.ServiceException
import com.whitesprite.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.whitesprite.dev.framework.common.exception.constants.GlobalErrorCodeConstants.SUCCESS
import com.whitesprite.dev.framework.common.exception.util.ErrorMessageFormatter
import io.micronaut.serde.annotation.Serdeable

/**
 * 通用返回结果类
 *
 * @param code 错误码
 * @param msg 错误信息
 * @param data 数据
 * @author WhiteSprite
 */
@Serdeable.Serializable
data class CommonResult<T>(
    /**
     * 错误码
     */
    val code: Int? = 0,
    /**
     * 错误信息
     */
    val msg: String? = "",
    /**
     * 数据
     */
    val data: T? = null
) {
    companion object {
        /**
         * 成功
         * @param data 数据
         * @return 结果
         */
        fun <T> success(data: T?): CommonResult<T> {
            return CommonResult(SUCCESS.code, SUCCESS.msg, data)
        }

        fun <T> error(code: Int?, msg: String?): CommonResult<T> {
            return CommonResult(code, msg, null)
        }

        fun <T> error(errorCode: ErrorCode): CommonResult<T> {
            return CommonResult(errorCode.code, errorCode.msg, null)
        }
    }
}

/**
 * 成功返回结果的便捷函数
 */
fun <T> success(data: T?): CommonResult<T> {
    return CommonResult.success(data)
}

fun <T> error(errorCode: ErrorCode): CommonResult<T> {
    return error(errorCode.code, errorCode.msg)
}

fun <T> error(code: Int?, msg: String?): CommonResult<T> {
    require(SUCCESS.code != code) { "Code 必须是错误的码！" }
    return CommonResult(code, msg, null)
}

/**
 * 支持可变参数的 error 方法
 */
fun <T> error(errorCode: ErrorCode, vararg params: Any?): CommonResult<T> {
    require(SUCCESS.code != errorCode.code) { "Code 必须是错误的码！" }
    val formattedMsg = ErrorMessageFormatter.format(errorCode.code, errorCode.msg, *params)
    return CommonResult(errorCode.code, formattedMsg, null)
}

/**
 * 支持CommonResult的error方法
 */
fun <T> error(result: CommonResult<*>): CommonResult<T> {
    return error(result.code, result.msg)
}

/**
 * 判断 CommonResult 是否成功
 */
fun isSuccess(code: Int?): Boolean {
    return code == SUCCESS.code
}

fun CommonResult<*>.isSuccess(): Boolean {
    return isSuccess(this.code)
}

/**
 * 判断 CommonResult 是否失败
 */
fun isError(result: CommonResult<*>?): Boolean {
    return result?.let { !it.isSuccess() } ?: true
}

fun CommonResult<*>.checkError() {
    if (isSuccess()) {
        return
    }
    throw ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR)
}

fun CommonResult<*>.getCheckedData(): Any? {
    checkError()
    return this.data
}

