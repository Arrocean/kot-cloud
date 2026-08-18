package com.arrocean.dev.gateway.route

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GatewayRouteResolverTest {

    @Test
    fun `uses the longest matching path prefix`() {
        val resolver = GatewayRouteResolver(
            listOf(
                GatewayRoute("api", "/api", "http://api.internal"),
                GatewayRoute("admin", "/api/admin", "http://admin.internal"),
            )
        )

        assertEquals("admin", resolver.resolve("/api/admin/users")?.route?.id)
    }

    @Test
    fun `does not treat a lexical prefix as a route match`() {
        val resolver = GatewayRouteResolver(
            listOf(GatewayRoute("api", "/api", "http://api.internal"))
        )

        assertNull(resolver.resolve("/api-v2/users"))
    }

    @Test
    fun `strips the matched prefix when configured`() {
        val resolver = GatewayRouteResolver(
            listOf(GatewayRoute("api", "/api", "http://api.internal", stripPrefix = true))
        )

        assertEquals("/users", resolver.resolve("/api/users")?.downstreamPath)
    }
}
