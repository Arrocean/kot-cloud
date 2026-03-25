package com.whitesprite.dev.framework.common.exception

/**
 * 服务异常类
 *
 * @property errorCode 错误码实体
 * @author WhiteSprite
 */
class ServiceException(
    /**
     * 业务错误码
     */
    val errorCode: ErrorCode
) : RuntimeException(errorCode.msg)