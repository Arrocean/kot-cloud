package com.whitesprite.dev.framework.security.core.password

import com.whitesprite.dev.framework.security.config.SecurityProperties

/**
 * 基于 PBKDF2 的密码编码器。
 *
 * 说明：
 * - 待实现，当前优先支持 Argon2id 和 BCrypt
 * - PBKDF2 在 Java 标准库中有支持，但性能和安全性不如 Argon2id 和 BCrypt，且配置较为复杂，因此暂不优先实现
 *
 * @author WhiteSprite
 */
class PBKDF2PasswordEncoder(
    private val properties: SecurityProperties.PBKDF2Properties,
) : PasswordEncoder {

    override fun encode(rawPassword: CharSequence): String {
        throw NotImplementedError("PBKDF2 密码编码器待实现")
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        throw NotImplementedError("PBKDF2 密码编码器待实现")
    }

}

///**
// * 基于 jBCrypt 的密码编码器。
// *
// * 说明：
// * - 作为兼容与切换方案保留
// * - 当 Argon2id 参数不满足当前运行环境要求时，可快速回退到 BCrypt
// *
// * @author WhiteSprite
// */
//open class BCryptPasswordEncoder(
//    private val properties: SecurityProperties.BCryptProperties,
//) : PasswordEncoder {
//
//    override fun encode(rawPassword: CharSequence): String {
//        validateProperties(properties)
//        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(properties.rounds))
//    }
//
//    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
//        return try {
//            BCrypt.checkpw(rawPassword.toString(), encodedPassword)
//        } catch (_: IllegalArgumentException) {
//            false
//        }
//    }
//
//    private fun validateProperties(properties: SecurityProperties.BCryptProperties) {
//        require(properties.rounds in 4..31) { "BCrypt rounds 必须在 4 到 31 之间" }
//    }
//}
//
