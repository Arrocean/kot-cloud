package com.arrocean.dev.gateway.config

import io.lettuce.core.RedisClient
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

/**
 * 创建 Gateway 专用 Redis 客户端。
 *
 * 连接在认证请求到达时建立，避免服务启动阶段强制要求 Redis 可用。
 */
@Factory
class GatewayRedisFactory {

    /**
     * 创建由 Micronaut 生命周期托管的 Redis 客户端。
     *
     * @param properties Gateway 配置，必须提供非空 Redis URI
     * @return 可供会话校验器按需创建连接的 Redis 客户端
     * @throws IllegalArgumentException Redis URI 为空时抛出
     */
    @Bean(preDestroy = "shutdown")
    @Singleton
    fun redisClient(properties: GatewayProperties): RedisClient {
        require(properties.redis.uri.isNotBlank()) { "kot.gateway.redis.uri must not be blank" }
        return RedisClient.create(properties.redis.uri.trim())
    }
}
