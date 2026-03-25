package com.whitesprite.dev.framework.web.core.handler

import com.whitesprite.dev.framework.common.exception.ErrorCode
import com.whitesprite.dev.framework.common.exception.ServiceException
import com.whitesprite.dev.framework.common.poko.CommonResult
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory

private fun toHttpStatus(code: Int): HttpStatus =
    runCatching { HttpStatus.valueOf(code) }.getOrElse { HttpStatus.INTERNAL_SERVER_ERROR }

@Singleton
class ServiceExceptionHandler :
    ExceptionHandler<ServiceException, HttpResponse<CommonResult<Nothing>>> {

    private val log = LoggerFactory.getLogger(ServiceExceptionHandler::class.java)

    override fun handle(request: HttpRequest<*>, ex: ServiceException): HttpResponse<CommonResult<Nothing>> {
        val ec: ErrorCode = ex.errorCode
        val status = toHttpStatus(ec.httpStatusCode)

        log.warn(
            "业务异常: method={}, uri={}, code={}, msg={}",
            request.method,
            request.uri,
            ec.code,
            ex.message
        )

        return HttpResponse.status<CommonResult<Nothing>>(status)
            .body(CommonResult.error<Nothing>(ec))
    }
}

@Singleton
class ValidationExceptionHandler :
    ExceptionHandler<ConstraintViolationException, HttpResponse<CommonResult<Nothing>>> {

    private val log = LoggerFactory.getLogger(ValidationExceptionHandler::class.java)

    override fun handle(request: HttpRequest<*>, ex: ConstraintViolationException): HttpResponse<CommonResult<Nothing>> {
        val msg = ex.constraintViolations
            .joinToString("; ") { v ->
                val field = v.propertyPath?.toString()?.substringAfterLast('.') ?: "param"
                "$field ${v.message}"
            }
            .ifBlank { "参数校验失败" }

        log.warn(
            "参数校验失败: method={}, uri={}, msg={}",
            request.method,
            request.uri,
            msg
        )

        return HttpResponse.badRequest<CommonResult<Nothing>>()
            .body(CommonResult.error<Nothing>(400, msg))
    }
}

@Singleton
class DefaultExceptionHandler :
    ExceptionHandler<Throwable, HttpResponse<CommonResult<Nothing>>> {

    private val log = LoggerFactory.getLogger(DefaultExceptionHandler::class.java)

    override fun handle(request: HttpRequest<*>, ex: Throwable): HttpResponse<CommonResult<Nothing>> {
        log.error("未处理的异常: method={}, uri={}", request.method, request.uri, ex)

        return HttpResponse.serverError<CommonResult<Nothing>>()
            .body(CommonResult.error<Nothing>(500, "系统异常"))
    }
}
