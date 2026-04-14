package com.whitesprite.dev.framework.security.config

import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.security.core.password.Argon2idPasswordEncoder
import com.whitesprite.dev.framework.security.core.password.BCryptPasswordEncoder
import com.whitesprite.dev.framework.security.core.password.PasswordEncoder
import com.whitesprite.dev.framework.security.core.password.PBKDF2PasswordEncoder
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.security.SecureRandom
import java.time.Clock

/**
 * 安全相关 Bean 工厂。
 *
 * 说明：
 * - 第一阶段只放最基础、最稳定的基础设施 Bean
 * - 复杂的认证提供器、401/403 统一处理、刷新令牌等后续再补
 *
 * @author WhiteSprite
 */
@Factory
class SecurityBeanFactory {

    /**
     * 统一时间源，方便后续 JWT、审计、测试场景复用。
     */
    @Singleton
    fun clock(): Clock {
        return Clock.systemUTC()
    }

    /**
     * 统一安全随机源。
     */
    @Singleton
    fun secureRandom(): SecureRandom {
        return SecureRandom()
    }

    /**
     * 统一密码编码器。
     *
     * 当前支持：
     * - argon2id（默认，推荐）
     * - bcrypt
     * - pbkdf2（PBKDF2-HMAC-SHA256）
     */
    @Singleton
    fun passwordEncoder(
        securityProperties: SecurityProperties,
        secureRandom: SecureRandom,
    ): PasswordEncoder {
        return when (securityProperties.password.encoder.trim().lowercase()) {
            "argon2id" -> Argon2idPasswordEncoder(securityProperties.password.argon2id, secureRandom)
            "bcrypt" -> BCryptPasswordEncoder(securityProperties.password.bcrypt)
            "pbkdf2" -> PBKDF2PasswordEncoder(securityProperties.password.pbkdf2, secureRandom)
            else -> throw ServiceExceptionFactory.configurationError(
                "不支持的密码编码算法: {}，当前仅支持 argon2id / bcrypt / pbkdf2",
                securityProperties.password.encoder
            )
        }
    }

    // TODO WhiteSprite：后续如引入 Micronaut 注解鉴权异常适配，可在 web 模块补充更薄的安全异常处理器
    // TODO WhiteSprite：补充 AuthenticationProvider，打通 AdminAuthController 登录链路
}


