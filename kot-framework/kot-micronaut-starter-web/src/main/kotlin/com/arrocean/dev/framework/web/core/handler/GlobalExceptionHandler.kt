package com.arrocean.dev.framework.web.core.handler

import com.arrocean.dev.framework.common.exception.ErrorCode
import com.arrocean.dev.framework.common.exception.ServiceException
import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.poko.CommonResult
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.bind.exceptions.UnsatisfiedArgumentException
import io.micronaut.core.convert.exceptions.ConversionErrorException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.codec.CodecException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.server.exceptions.ConversionErrorHandler
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.HttpStatusHandler
import io.micronaut.http.server.exceptions.UnsatisfiedArgumentHandler
import io.micronaut.http.server.exceptions.UnsatisfiedRouteHandler
import io.micronaut.json.JsonSyntaxException
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException

private fun toHttpStatus(code: Int): HttpStatus =
    runCatching { HttpStatus.valueOf(code) }.getOrElse { HttpStatus.INTERNAL_SERVER_ERROR }

private fun buildErrorResponse(errorCode: ErrorCode): HttpResponse<CommonResult<Nothing>> {
    val status = toHttpStatus(errorCode.httpStatusCode)
    return HttpResponse.status<CommonResult<Nothing>>(status)
        .body(CommonResult.error<Nothing>(errorCode))
}

private fun buildErrorResponse(errorCode: ErrorCode, message: String): HttpResponse<CommonResult<Nothing>> {
    val resolved = if (message.isBlank() || message == errorCode.msg) {
        errorCode
    } else {
        errorCode.copy(msg = message)
    }
    return buildErrorResponse(resolved)
}

private fun resolveHttpStatusErrorCode(status: HttpStatus, message: String?): ErrorCode {
    val resolvedMessage = message?.takeIf(String::isNotBlank)
    return when (status) {
        HttpStatus.BAD_REQUEST -> GlobalErrorCodeConstants.BAD_REQUEST.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.BAD_REQUEST.msg)
        HttpStatus.UNAUTHORIZED -> GlobalErrorCodeConstants.UNAUTHORIZED.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.UNAUTHORIZED.msg)
        HttpStatus.FORBIDDEN -> GlobalErrorCodeConstants.FORBIDDEN.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.FORBIDDEN.msg)
        HttpStatus.NOT_FOUND -> GlobalErrorCodeConstants.NOT_FOUND.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.NOT_FOUND.msg)
        HttpStatus.METHOD_NOT_ALLOWED -> GlobalErrorCodeConstants.METHOD_NOT_ALLOWED.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.METHOD_NOT_ALLOWED.msg)
        HttpStatus.LOCKED -> GlobalErrorCodeConstants.LOCKED.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.LOCKED.msg)
        HttpStatus.NOT_IMPLEMENTED -> GlobalErrorCodeConstants.NOT_IMPLEMENTED.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.NOT_IMPLEMENTED.msg)
        HttpStatus.TOO_MANY_REQUESTS -> GlobalErrorCodeConstants.TOO_MANY_REQUESTS.copy(msg = resolvedMessage ?: GlobalErrorCodeConstants.TOO_MANY_REQUESTS.msg)
        HttpStatus.UNSUPPORTED_MEDIA_TYPE -> ErrorCode(
            code = HttpStatus.UNSUPPORTED_MEDIA_TYPE.code,
            msg = resolvedMessage ?: "请求内容类型不支持",
            httpStatusCode = HttpStatus.UNSUPPORTED_MEDIA_TYPE.code,
        )
        else -> ErrorCode(
            code = status.code,
            msg = resolvedMessage ?: status.reason,
            httpStatusCode = status.code,
        )
    }
}

private fun HttpRequest<*>.summary(): String = "method=$method, uri=$uri"

private fun findServiceException(ex: Throwable): ServiceException? {
    var current: Throwable? = ex
    while (current != null) {
        if (current is ServiceException) {
            return current
        }
        current = current.cause
    }
    return null
}

private fun formatConstraintViolationMessage(ex: ConstraintViolationException): String {
    return ex.constraintViolations
        .joinToString("; ") { violation ->
            val field = violation.propertyPath?.toString()?.substringAfterLast('.') ?: "param"
            "$field ${violation.message}"
        }
        .ifBlank { "请求参数不正确" }
}

private fun formatUnsatisfiedArgumentMessage(argumentName: String): String {
    return "请求参数缺失: $argumentName"
}

private fun formatConversionMessage(ex: ConversionErrorException): String {
    val argumentName = ex.argument.name
    val originalValue = ex.conversionError.originalValue.orElse(null)?.toString()?.takeIf(String::isNotBlank)
    return if (originalValue != null) {
        "请求参数类型错误: $argumentName=$originalValue"
    } else {
        "请求参数类型错误: $argumentName"
    }
}

private fun formatCodecMessage(message: String?): String {
    val detail = message?.lineSequence()?.firstOrNull()?.trim()?.takeIf(String::isNotBlank)
    return if (detail == null) {
        "请求体格式不正确"
    } else {
        "请求体格式不正确: $detail"
    }
}

@Singleton
class ServiceExceptionHandler :
    ExceptionHandler<ServiceException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: ServiceException): HttpResponse<CommonResult<Nothing>> {
        val ec: ErrorCode = ex.errorCode

        log.warn {
            "[业务异常]: ${request.summary()}, code=${ec.code}, msg=${ex.message}"
        }

        return buildErrorResponse(ec)
    }
}

@Singleton
@Replaces(ConstraintExceptionHandler::class)
class ValidationExceptionHandler :
    ExceptionHandler<ConstraintViolationException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: ConstraintViolationException): HttpResponse<CommonResult<Nothing>> {
        val msg = formatConstraintViolationMessage(ex)

        log.warn {
            "参数校验失败: ${request.summary()}, msg=$msg"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, msg)
    }
}

@Singleton
@Replaces(UnsatisfiedRouteHandler::class)
class UnsatisfiedRouteExceptionHandler :
    ExceptionHandler<UnsatisfiedRouteException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: UnsatisfiedRouteException): HttpResponse<CommonResult<Nothing>> {
        val message = formatUnsatisfiedArgumentMessage(ex.argument.name)

        log.warn {
            "路由参数缺失: ${request.summary()}, msg=$message"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, message)
    }
}

@Singleton
@Replaces(UnsatisfiedArgumentHandler::class)
class UnsatisfiedArgumentExceptionHandler :
    ExceptionHandler<UnsatisfiedArgumentException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: UnsatisfiedArgumentException): HttpResponse<CommonResult<Nothing>> {
        val message = formatUnsatisfiedArgumentMessage(ex.argument.name)

        log.warn {
            "请求参数缺失: ${request.summary()}, msg=$message"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, message)
    }
}

@Singleton
@Replaces(ConversionErrorHandler::class)
class ConversionErrorExceptionHandler :
    ExceptionHandler<ConversionErrorException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: ConversionErrorException): HttpResponse<CommonResult<Nothing>> {
        val message = formatConversionMessage(ex)

        log.warn {
            "请求参数类型错误: ${request.summary()}, msg=$message"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, message)
    }
}

@Singleton
class CodecExceptionHandler :
    ExceptionHandler<CodecException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: CodecException): HttpResponse<CommonResult<Nothing>> {
        val message = formatCodecMessage(ex.message)

        log.warn(ex) {
            "请求体解码失败: ${request.summary()}, msg=$message"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, message)
    }
}

@Singleton
class JsonSyntaxExceptionHandler :
    ExceptionHandler<JsonSyntaxException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: JsonSyntaxException): HttpResponse<CommonResult<Nothing>> {
        val message = formatCodecMessage(ex.message)

        log.warn(ex) {
            "请求体 JSON 语法错误: ${request.summary()}, msg=$message"
        }

        return buildErrorResponse(GlobalErrorCodeConstants.BAD_REQUEST, message)
    }
}

@Singleton
@Replaces(HttpStatusHandler::class)
class HttpStatusExceptionHandler :
    ExceptionHandler<HttpStatusException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: HttpStatusException): HttpResponse<CommonResult<Nothing>> {
        val errorCode = resolveHttpStatusErrorCode(ex.status, ex.message)

        log.warn {
            "HTTP 状态异常: ${request.summary()}, status=${ex.status.code}, msg=${errorCode.msg}"
        }

        return buildErrorResponse(errorCode)
    }
}

@Singleton
class DefaultExceptionHandler :
    ExceptionHandler<Throwable, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(request: HttpRequest<*>, ex: Throwable): HttpResponse<CommonResult<Nothing>> {
        val serviceException = findServiceException(ex)
        if (serviceException != null) {
            val errorCode = serviceException.errorCode

            log.warn(ex) {
                "捕获到包装业务异常: ${request.summary()}, code=${errorCode.code}, msg=${serviceException.message}"
            }

            return buildErrorResponse(errorCode)
        }

        log.error(ex) { "未处理的异常: ${request.summary()}" }

        return buildErrorResponse(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR)
    }
}
