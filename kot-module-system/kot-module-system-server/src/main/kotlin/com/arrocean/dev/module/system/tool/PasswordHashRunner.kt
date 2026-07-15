package com.arrocean.dev.module.system.tool

import com.arrocean.dev.framework.security.config.SecurityProperties
import com.arrocean.dev.framework.security.core.password.Argon2idPasswordEncoder
import com.arrocean.dev.framework.security.core.password.BCryptPasswordEncoder
import com.arrocean.dev.framework.security.core.password.PasswordEncoder
import com.arrocean.dev.framework.security.core.password.PBKDF2PasswordEncoder
import java.security.SecureRandom

/**
 * 本地密码哈希生成小工具。
 *
 * 用途：
 * - 在尚未打通登录前，先为数据库中的管理员账号生成可用的密码哈希
 * - 不启动 Micronaut，不依赖登录态，不触发数据库 / Redis / Web 安全链路
 *
 * 默认值：
 * - username = admin
 * - password = Admin@123456
 * - encoder = pbkdf2
 *
 * 可选参数：
 * - --username=admin
 * - --password=Admin@123456
 * - --encoder=pbkdf2|bcrypt|argon2id
 */
object PasswordHashRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        val options = args
            .mapNotNull { arg ->
                if (!arg.startsWith("--") || !arg.contains('=')) {
                    null
                } else {
                    val kv = arg.removePrefix("--").split('=', limit = 2)
                    kv[0] to kv[1]
                }
            }
            .toMap()

        val username = options["username"]?.takeIf(String::isNotBlank) ?: "admin"
        val password = options["password"]?.takeIf(String::isNotBlank) ?: "Admin@123456"
        val encoderName = options["encoder"]?.trim()?.lowercase()?.takeIf(String::isNotBlank) ?: "pbkdf2"

        val securityProperties = buildSecurityProperties(encoderName)
        val passwordEncoder = buildPasswordEncoder(securityProperties, SecureRandom())
        val encodedPassword = passwordEncoder.encode(password)
        val matches = passwordEncoder.matches(password, encodedPassword)

        println("=== Password Hash Runner ===")
        println("username      : $username")
        println("raw password  : $password")
        println("encoder       : ${securityProperties.password.encoder}")
        println("password hash : $encodedPassword")
        println("self-check    : $matches")
        println()
        println("SQL 示例：")
        println("update system_users set password_hash = '$encodedPassword' where username = '$username';")
    }

    private fun buildSecurityProperties(encoderName: String): SecurityProperties {
        return SecurityProperties().apply {
            password.encoder = encoderName

            // 对齐当前主服务默认配置（kot-server/application.properties）
            password.pbkdf2.iterations = 185000
            password.pbkdf2.hashLength = 32
            password.pbkdf2.saltLength = 16

            password.bcrypt.rounds = 10

            password.argon2id.saltLength = 16
            password.argon2id.hashLength = 32
            password.argon2id.memoryKb = 65536
            password.argon2id.iterations = 3
            password.argon2id.parallelism = 1
        }
    }

    private fun buildPasswordEncoder(
        securityProperties: SecurityProperties,
        secureRandom: SecureRandom,
    ): PasswordEncoder {
        return when (securityProperties.password.encoder.trim().lowercase()) {
            "argon2id" -> Argon2idPasswordEncoder(securityProperties.password.argon2id, secureRandom)
            "bcrypt" -> BCryptPasswordEncoder(securityProperties.password.bcrypt)
            "pbkdf2" -> PBKDF2PasswordEncoder(securityProperties.password.pbkdf2, secureRandom)
            else -> error("不支持的密码编码算法: ${securityProperties.password.encoder}，仅支持 pbkdf2 / bcrypt / argon2id")
        }
    }
}


