package com.arrocean.dev.gateway.security

import com.arrocean.dev.framework.common.enums.CommonUserTypeEnum
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class GatewayAuthenticationServiceTest {

    @Test
    fun `creates the authentication service with its security collaborators`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1", CommonUserTypeEnum.USER) },
            sessionValidator = GatewaySessionValidator { true },
        )

        assertNotNull(service)
    }

    @Test
    fun `accepts a verified token with an active Redis session`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1", CommonUserTypeEnum.USER) },
            sessionValidator = GatewaySessionValidator { true },
        )

        assertEquals("session-1", service.authenticate("Bearer token").sessionId)
    }

    @Test
    fun `rejects a token whose Redis session is absent`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1", CommonUserTypeEnum.USER) },
            sessionValidator = GatewaySessionValidator { false },
        )

        assertFailsWith<GatewayAuthenticationException> {
            service.authenticate("Bearer token")
        }
    }

    @Test
    fun `rejects a member token on an admin route`() {
        val service = GatewayAuthenticationService(
            tokenVerifier = GatewayTokenVerifier { GatewayPrincipal("token", "session-1", CommonUserTypeEnum.USER) },
            sessionValidator = GatewaySessionValidator { true },
        )

        assertFailsWith<GatewayIdentityVerificationException> {
            service.authenticate("Bearer token", setOf(CommonUserTypeEnum.ADMIN.name))
        }
    }
}
