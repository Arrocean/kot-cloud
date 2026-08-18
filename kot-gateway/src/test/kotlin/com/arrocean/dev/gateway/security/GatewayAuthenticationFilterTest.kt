package com.arrocean.dev.gateway.security

import com.arrocean.dev.gateway.config.GatewayProperties
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import reactor.core.publisher.Mono
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GatewayAuthenticationFilterTest {

    @Test
    fun `allows configured public paths without authentication`() {
        val filter = filter()

        val response = Mono.from<io.micronaut.http.MutableHttpResponse<*>>(
            filter.doFilter(HttpRequest.GET<Any>("/health"), chain())
        ).block()

        assertEquals(HttpStatus.NO_CONTENT, response?.status)
    }

    @Test
    fun `allows CORS preflight requests without authentication`() {
        val filter = filter()

        val response = Mono.from<io.micronaut.http.MutableHttpResponse<*>>(
            filter.doFilter(HttpRequest.OPTIONS<Any>("/admin-api/system/users"), chain())
        ).block()

        assertEquals(HttpStatus.NO_CONTENT, response?.status)
    }

    @Test
    fun `rejects requests with no bearer token`() {
        val filter = filter()

        val response = Mono.from<io.micronaut.http.MutableHttpResponse<*>>(
            filter.doFilter(HttpRequest.GET<Any>("/admin-api/system/users"), chain())
        ).block()

        assertEquals(HttpStatus.UNAUTHORIZED, response?.status)
    }

    @Test
    fun `propagates Redis unavailability for global error mapping`() {
        val filter = GatewayAuthenticationFilter(
            properties = GatewayProperties(),
            authenticationService = GatewayAuthenticationService(
                tokenVerifier = GatewayTokenVerifier { GatewayPrincipal(it, "session-1") },
                sessionValidator = GatewaySessionValidator { throw GatewaySessionUnavailableException(IllegalStateException()) },
            ),
        )

        assertFailsWith<GatewaySessionUnavailableException> {
            Mono.from<io.micronaut.http.MutableHttpResponse<*>>(
                filter.doFilter(HttpRequest.GET<Any>("/admin-api/system/users").header("Authorization", "Bearer token"), chain())
            ).block()
        }
    }

    private fun filter(): GatewayAuthenticationFilter {
        return GatewayAuthenticationFilter(
            properties = GatewayProperties(),
            authenticationService = GatewayAuthenticationService(
                tokenVerifier = GatewayTokenVerifier { GatewayPrincipal(it, "session-1") },
                sessionValidator = GatewaySessionValidator { true },
            ),
        )
    }

    private fun chain(): io.micronaut.http.filter.ServerFilterChain =
        io.micronaut.http.filter.ServerFilterChain { _: HttpRequest<*> ->
            Mono.just<io.micronaut.http.MutableHttpResponse<*>>(io.micronaut.http.HttpResponse.noContent<Any>())
        }
}
