package com.arrocean.dev.gateway.cors

import com.arrocean.dev.gateway.config.GatewayProperties
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayCorsPolicyTest {

    @Test
    fun `responds to allowed preflight requests without invoking the remaining chain`() {
        val properties = GatewayProperties().apply {
            cors.allowedOrigins = listOf("https://console.example.com")
        }
        val filter = GatewayCorsFilter(properties)
        val request = HttpRequest.OPTIONS<Any>("/admin-api/system/users")
            .header("Origin", "https://console.example.com")
            .header("Access-Control-Request-Method", "GET")

        val response = Mono.from<io.micronaut.http.MutableHttpResponse<*>>(
            filter.doFilter(request, throwingChain())
        ).block()

        assertEquals(HttpStatus.NO_CONTENT, response?.status)
        assertEquals("https://console.example.com", response?.header("Access-Control-Allow-Origin"))
    }

    private fun throwingChain(): io.micronaut.http.filter.ServerFilterChain =
        io.micronaut.http.filter.ServerFilterChain { _: HttpRequest<*> ->
            Mono.error<io.micronaut.http.MutableHttpResponse<*>>(IllegalStateException("Filter chain should not run")) as Publisher<io.micronaut.http.MutableHttpResponse<*>>
        }
}
