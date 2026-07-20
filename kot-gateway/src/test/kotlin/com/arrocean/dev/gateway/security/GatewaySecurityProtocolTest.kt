package com.arrocean.dev.gateway.security

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GatewaySecurityProtocolTest {

    @Test
    fun `builds stable Redis session keys`() {
        assertEquals(
            "kot:security:session:session-123",
            GatewaySessionKeyFactory.sessionKey("kot:security", "session-123"),
        )
    }

    @Test
    fun `normalizes public path patterns`() {
        val matcher = PublicPathMatcher(listOf("/health", "/admin-api/system/auth/**"))

        assertEquals(true, matcher.matches("/health"))
        assertEquals(true, matcher.matches("/admin-api/system/auth/login"))
        assertEquals(false, matcher.matches("/admin-api/system/users"))
    }

    @Test
    fun `extracts a bearer token without altering its value`() {
        assertEquals("header.payload.signature", BearerTokenReader.extract("Bearer header.payload.signature"))
    }

    @Test
    fun `returns null when authorization is not bearer authentication`() {
        assertNull(BearerTokenReader.extract("Basic dXNlcjpwYXNz"))
    }

    @Test
    fun `rejects an empty session id when building a session key`() {
        assertFailsWith<IllegalArgumentException> {
            GatewaySessionKeyFactory.sessionKey("kot:security", " ")
        }
    }
}
