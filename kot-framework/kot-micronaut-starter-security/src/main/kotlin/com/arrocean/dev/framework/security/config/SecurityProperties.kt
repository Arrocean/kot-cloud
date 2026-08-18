package com.arrocean.dev.framework.security.config

import io.micronaut.context.annotation.ConfigurationProperties

/**
 * 安全模块配置。
 *
 * 说明：
 * - `micronaut.security.*` 负责驱动 Micronaut Security / JWT 的底层行为
 * - `kot.security.*` 负责承载项目自定义的安全参数
 *
 * @author WhiteSprite
 */
@ConfigurationProperties("kot.security")
open class SecurityProperties {

    /**
     * Access Token 过期时间，单位：秒
     */
    var accessTokenExpireSeconds: Int = 7200

    /**
     * Refresh Token 过期时间，单位：秒。
     */
    var refreshTokenExpireSeconds: Int = 604800

    /**
     * 默认客户端标识。
     */
    var defaultClientId: String = "default"

    /**
     * Redis 会话配置。
     */
    var redis: RedisProperties = RedisProperties()

    /**
     * 密码编码配置。
     */
    var password: PasswordProperties = PasswordProperties()

    @ConfigurationProperties("password")
    open class PasswordProperties {

        /**
         * 默认编码算法。
         *
         * 支持：argon2id / bcrypt / pbkdf2
         */
        var encoder: String = "argon2id"

        /**
         * Argon2id 参数。
         */
        var argon2id: Argon2idProperties = Argon2idProperties()

        /**
         * BCrypt 参数。
         */
        var bcrypt: BCryptProperties = BCryptProperties()

        /**
         * PBKDF2-HMAC-SHA256 参数。
         */
        var pbkdf2: PBKDF2Properties = PBKDF2Properties()
    }

    @ConfigurationProperties("argon2id")
    open class Argon2idProperties {

        /**
         * 随机盐长度（字节）。
         */
        var saltLength: Int = 16

        /**
         * 输出哈希长度（字节）。
         */
        var hashLength: Int = 32

        /**
         * 内存成本（KB）。
         */
        var memoryKb: Int = 65536

        /**
         * 迭代次数。
         */
        var iterations: Int = 3

        /**
         * 并行度。
         */
        var parallelism: Int = 1
    }

    @ConfigurationProperties("bcrypt")
    open class BCryptProperties {

        /**
         * BCrypt 成本 / 加密轮次。
         */
        var rounds: Int = 10
    }

    @ConfigurationProperties("pbkdf2")
    open class PBKDF2Properties {

        /**
         * 迭代次数。
         */
        var iterations: Int = 185000

        /**
         * 输出哈希长度（字节）。
         */
        var hashLength: Int = 32

        /**
         * 随机盐长度（字节）。
         */
        var saltLength: Int = 16
    }

    @ConfigurationProperties("redis")
    open class RedisProperties {

        /**
         * Redis Key 前缀。
         *
         * 说明：连接地址由 Micronaut Redis 模块的标准配置 `redis.uri` 提供，
         * 此处只保留业务自定义的 Key 前缀。
         */
        var keyPrefix: String = "kot:security"
    }
}

