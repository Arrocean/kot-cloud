package com.arrocean.dev.gateway.security

import io.micronaut.context.ApplicationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class GatewayAuthenticationServiceTest {

    @Test
    fun `registers the authentication service as a Micronaut bean`() {
        // micronaut-redis 仅在存在 redis.* 配置时才创建 RedisClient Bean；
        // 提供占位 URI 即可完成 Bean 装配（Lettuce 连接为惰性建立，测试无需真实 Redis）
        ApplicationContext.run(mapOf("redis.uri" to "redis://127.0.0.1:6379/0")).use { context ->
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
