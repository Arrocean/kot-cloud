package com.arrocean.dev.gateway.handler

import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.gateway.security.GatewayAuthenticationException
import com.arrocean.dev.gateway.security.GatewayIdentityVerificationException
import com.arrocean.dev.gateway.security.GatewaySessionUnavailableException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import java.net.SocketTimeoutException

/**
 * 网关层未处理异常的统一 HTTP 响应映射。
 *
 * 下游正常返回的 4xx/5xx 由代理直接透传；只有认证、Redis 和下游连接等网关错误才在这里转换。
 */
@Singleton
class GatewayExceptionHandler : ExceptionHandler<Throwable, HttpResponse<CommonResult<Nothing>>> {

    private val log = KotlinLogging.logger {}

    /**
     * 将网关异常转换为不泄露下游实现细节的标准错误响应。
     *
     * @param request 触发异常的入站请求，用于记录诊断日志
     * @param exception 未被过滤器或代理正常处理的异常
     * @return 携带 CommonResult 错误体的 HTTP 响应
     */
    override fun handle(request: HttpRequest<*>, exception: Throwable): HttpResponse<CommonResult<Nothing>> {
        val (status, code, message) = when {
            exception.hasCause<SocketTimeoutException>() -> Triple(HttpStatus.GATEWAY_TIMEOUT, 504, "下游服务响应超时")
            else -> when (exception) {
            is GatewayIdentityVerificationException -> Triple(HttpStatus.FORBIDDEN, 403, "身份验证不正确")
            is GatewayAuthenticationException -> Triple(HttpStatus.UNAUTHORIZED, 401, "认证失败")
            is GatewaySessionUnavailableException -> Triple(HttpStatus.SERVICE_UNAVAILABLE, 503, "认证服务暂不可用")
            is HttpClientException -> Triple(HttpStatus.BAD_GATEWAY, 502, "下游服务不可用")
            else -> Triple(HttpStatus.INTERNAL_SERVER_ERROR, 500, "网关内部错误")
            }
        }
        if (status.code >= 500) {
            log.error(exception) { "网关异常: method=${request.method}, uri=${request.uri}, status=${status.code}" }
        } else {
            log.warn { "网关请求失败: method=${request.method}, uri=${request.uri}, status=${status.code}" }
        }
        return HttpResponse.status<CommonResult<Nothing>>(status)
            .body(CommonResult.error(code, message))
    }
}

/** 在异常因果链中查找指定异常类型。 */
private inline fun <reified T : Throwable> Throwable.hasCause(): Boolean {
    var current: Throwable? = this
    while (current != null) {
        if (current is T) return true
        current = current.cause
    }
    return false
}
