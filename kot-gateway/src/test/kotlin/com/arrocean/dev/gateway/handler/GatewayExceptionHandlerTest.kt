package com.arrocean.dev.gateway.handler

import com.arrocean.dev.gateway.security.GatewayAuthenticationException
import com.arrocean.dev.gateway.security.GatewaySessionUnavailableException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import java.net.SocketTimeoutException
import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayExceptionHandlerTest {

    private val handler = GatewayExceptionHandler()
    private val request = HttpRequest.GET<Any>("/admin-api/system/users")

    @Test
    fun `maps authentication errors to 401`() {
        assertEquals(HttpStatus.UNAUTHORIZED, handler.handle(request, GatewayAuthenticationException("invalid")).status)
    }

    @Test
    fun `maps Redis availability errors to 503`() {
        assertEquals(
            HttpStatus.SERVICE_UNAVAILABLE,
            handler.handle(request, GatewaySessionUnavailableException(IllegalStateException())).status,
        )
    }

    @Test
    fun `maps downstream client errors to 502`() {
        assertEquals(HttpStatus.BAD_GATEWAY, handler.handle(request, HttpClientException("refused")).status)
    }

    @Test
    fun `maps nested downstream timeout to 504`() {
        assertEquals(
            HttpStatus.GATEWAY_TIMEOUT,
            handler.handle(request, HttpClientException("timeout", SocketTimeoutException())).status,
        )
    }
}
