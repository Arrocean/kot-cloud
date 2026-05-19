package com.arrocean.dev.framework.security.config

import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import io.lettuce.core.RedisClient
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

/**
 * 安全模块 Redis Bean 工厂。
 *
 * 仅提供客户端，具体连接在使用时按需建立，
 * 避免在未实际触发登录前强依赖 Redis 可用。
 */
@Factory
class SecurityRedisFactory {

    @Bean(preDestroy = "shutdown")
    @Singleton
    fun redisClient(securityProperties: SecurityProperties): RedisClient {
        val uri = securityProperties.redis.uri.trim()
        if (uri.isBlank()) {
            throw ServiceExceptionFactory.configurationError("kot.security.redis.uri 不能为空")
        }
        return RedisClient.create(uri)
    }
}

