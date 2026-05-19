package com.arrocean.dev.framework.security.core.password

import com.arrocean.dev.framework.security.config.SecurityProperties
import org.mindrot.jbcrypt.BCrypt

/**
 * 基于 jBCrypt 的密码编码器。
 *
 * 说明：
 * - 作为兼容与切换方案保留
 * - 当 Argon2id 参数不满足当前运行环境要求时，可快速回退到 BCrypt
 *
 * @author WhiteSprite
 */
open class BCryptPasswordEncoder(
    private val properties: SecurityProperties.BCryptProperties,
) : PasswordEncoder {

    override fun encode(rawPassword: CharSequence): String {
        validateProperties(properties)
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(properties.rounds))
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return try {
            BCrypt.checkpw(rawPassword.toString(), encodedPassword)
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    private fun validateProperties(properties: SecurityProperties.BCryptProperties) {
        require(properties.rounds in 4..31) { "BCrypt rounds 必须在 4 到 31 之间" }
    }
}



