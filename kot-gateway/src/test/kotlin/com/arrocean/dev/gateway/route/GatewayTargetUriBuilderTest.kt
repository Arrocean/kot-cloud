package com.arrocean.dev.gateway.route

import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayTargetUriBuilderTest {

    @Test
    fun `preserves the downstream base path and original query string`() {
        val target = GatewayTargetUriBuilder.build(
            baseUri = "http://system.internal:8080/internal",
            downstreamPath = "/admin/users",
            rawQuery = "page=1&size=20",
        )

        assertEquals("http://system.internal:8080/internal/admin/users?page=1&size=20", target.toString())
    }
}
