package com.whitesprite.dev.framework.common.exception

/**
 * 错误码类
 *
 * @property code 错误码
 * @property msg 错误信息
 * @author WhiteSprite
 */
data class ErrorCode(
    /**
     * 错误码
     *
     * @see [com.whitesprite.dev.framework.common.poko.CommonResult.code]
     */
    val code: Int,
    /**
     * 错误信息
     *
     * @see [com.whitesprite.dev.framework.common.poko.CommonResult.msg]
     */
    val msg: String,

    /**
     * HTTP 状态码
     *
     * @see []
     */
    val httpStatusCode: Int = 500
)