package com.whitesprite.dev.framework.common.exception

/**
 * 错误码类
 *
 * @param code 错误码
 * @param msg 错误信息
 * @author WhiteSprite
 */
class ErrorCode(
    /**
     * 错误码
     *
     * @see [com.whitesprite.dev.framework.common.poko.CommonResult.code]
     */
    val code: Int?,
    /**
     * 错误信息
     *
     * @see [com.whitesprite.dev.framework.common.poko.CommonResult.msg]
     */
    val msg: String?
)