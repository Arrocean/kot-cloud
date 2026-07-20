package com.arrocean.dev.gateway.security

import io.micronaut.context.ApplicationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class GatewayAuthenticationServiceTest {

    @Test
    fun `registers the authentication service as a Micronaut bean`() {
        ApplicationContext.run().use { context ->
            assertNotNull(context.findBean(GatewayAuthenticationService::class.java).orElse(null))
        }
    }

    @Test
    fun `accepts a verified token with an active Redis session`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1") },
            sessionValidator = GatewaySessionValidator { true },
        )

        assertEquals("session-1", service.authenticate("Bearer token").sessionId)
    }

    @Test
    fun `rejects a token whose Redis session is absent`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1") },
            sessionValidator = GatewaySessionValidator { false },
        )

        assertFailsWith<GatewayAuthenticationException> {
            service.authenticate("Bearer token")
        }
    }
}
