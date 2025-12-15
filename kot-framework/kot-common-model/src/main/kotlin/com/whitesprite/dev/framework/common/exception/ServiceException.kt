package com.whitesprite.dev.framework.common.exception

/**
 * 服务异常类
 *
 * @param code 错误码
 * @param message 错误信息
 * @author WhiteSprite
 */
class ServiceException(
    /**
     * 错误码
     */
    val code: Int,
    /**
     * 错误信息
     */
    override val message: String
) : RuntimeException(message)