package com.arrocean.dev.framework.security.handler

import com.arrocean.dev.framework.common.exception.ErrorCode
import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.poko.CommonResult
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationExceptionHandler
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import jakarta.inject.Singleton

private fun buildSecurityErrorResponse(errorCode: ErrorCode): HttpResponse<CommonResult<Nothing>> {
    val status = runCatching { HttpStatus.valueOf(errorCode.httpStatusCode) }
        .getOrElse { HttpStatus.INTERNAL_SERVER_ERROR }
    return HttpResponse.status<CommonResult<Nothing>>(status)
        .body(CommonResult.error(errorCode))
}

@Singleton
@Replaces(AuthenticationExceptionHandler::class)
class BusinessAuthenticationExceptionHandler :
    ExceptionHandler<AuthenticationException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(
        request: HttpRequest<*>,
        exception: AuthenticationException,
    ): HttpResponse<CommonResult<Nothing>> {
        log.warn {
            "认证失败: method=${request.method}, uri=${request.uri}, msg=${exception.message ?: GlobalErrorCodeConstants.UNAUTHORIZED.msg}"
        }
        return buildSecurityErrorResponse(GlobalErrorCodeConstants.UNAUTHORIZED)
    }
}

@Singleton
@Replaces(DefaultAuthorizationExceptionHandler::class)
class BusinessAuthorizationExceptionHandler :
    ExceptionHandler<AuthorizationException, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    override fun handle(
        request: HttpRequest<*>,
        exception: AuthorizationException,
    ): HttpResponse<CommonResult<Nothing>> {
        val errorCode = if (exception.isForbidden) {
            GlobalErrorCodeConstants.FORBIDDEN
        } else {
            GlobalErrorCodeConstants.UNAUTHORIZED
        }
        log.warn {
            "访问被拒绝: method=${request.method}, uri=${request.uri}, forbidden=${exception.isForbidden}, msg=${errorCode.msg}"
        }
        return buildSecurityErrorResponse(errorCode)
    }
}


