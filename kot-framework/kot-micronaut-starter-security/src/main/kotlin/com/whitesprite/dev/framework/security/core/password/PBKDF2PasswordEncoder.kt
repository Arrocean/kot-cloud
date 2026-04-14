package com.whitesprite.dev.framework.security.core.password

import com.whitesprite.dev.framework.security.config.SecurityProperties
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * 基于 PBKDF2-HMAC-SHA256 的密码编码器。
 *
 * 输出格式：
 * `$pbkdf2-sha256$i=<iterations>,l=<hashLength>$<salt>$<hash>`
 *
 * @author WhiteSprite
 */
open class PBKDF2PasswordEncoder(
    private val properties: SecurityProperties.PBKDF2Properties,
    private val secureRandom: SecureRandom,
) : PasswordEncoder {

    override fun encode(rawPassword: CharSequence): String {
        validateProperties(properties)

        val salt = ByteArray(properties.saltLength)
        secureRandom.nextBytes(salt)

        val hash = hash(rawPassword, salt, properties.iterations, properties.hashLength)
        return buildEncodedPassword(properties, salt, hash)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        val parsed = parseEncodedPassword(encodedPassword) ?: return false
        val actualHash = hash(rawPassword, parsed.salt, parsed.parameters.iterations, parsed.parameters.hashLength)
        return MessageDigest.isEqual(parsed.hash, actualHash)
    }

}

private fun hash(
    rawPassword: CharSequence,
    salt: ByteArray,
    iterations: Int,
    hashLength: Int,
): ByteArray {
    val keySpec = PBEKeySpec(rawPassword.toString().toCharArray(), salt, iterations, hashLength * BITS_PER_BYTE)
    return try {
        SecretKeyFactory.getInstance(PBKDF2_SHA256_ALGORITHM)
            .generateSecret(keySpec)
            .encoded
    } finally {
        keySpec.clearPassword()
    }
}

private fun buildEncodedPassword(
    properties: SecurityProperties.PBKDF2Properties,
    salt: ByteArray,
    hash: ByteArray,
): String {
    val saltValue = salt.encodeBase64WithoutPadding()
    val hashValue = hash.encodeBase64WithoutPadding()
    return "\$pbkdf2-sha256\$i=${properties.iterations},l=${properties.hashLength}\$$saltValue\$$hashValue"
}

private fun parseEncodedPassword(encodedPassword: String): ParsedPBKDF2Password? {
    val parts = encodedPassword.split('$')
    if (parts.size != 5) {
        return null
    }
    if (parts[1] != "pbkdf2-sha256") {
        return null
    }

    val parameterMap = parts[2]
        .split(',')
        .mapNotNull { token ->
            val kv = token.split('=', limit = 2)
            if (kv.size != 2) {
                null
            } else {
                kv[0] to kv[1]
            }
        }
        .toMap()

    val iterations = parameterMap["i"]?.toIntOrNull() ?: return null
    val hashLength = parameterMap["l"]?.toIntOrNull() ?: return null
    val salt = parts[3].decodeBase64WithoutPadding() ?: return null
    val hash = parts[4].decodeBase64WithoutPadding() ?: return null
    if (hash.size != hashLength) {
        return null
    }

    val parsedProperties = SecurityProperties.PBKDF2Properties().apply {
        this.iterations = iterations
        this.hashLength = hashLength
        this.saltLength = salt.size
    }
    validateProperties(parsedProperties)

    return ParsedPBKDF2Password(
        parameters = parsedProperties,
        salt = salt,
        hash = hash,
    )
}

private fun validateProperties(properties: SecurityProperties.PBKDF2Properties) {
    require(properties.iterations > 0) { "PBKDF2 iterations 必须大于 0" }
    require(properties.hashLength >= 16) { "PBKDF2 hashLength 不能小于 16" }
    require(properties.saltLength >= 8) { "PBKDF2 saltLength 不能小于 8" }
}

private data class ParsedPBKDF2Password(
    val parameters: SecurityProperties.PBKDF2Properties,
    val salt: ByteArray,
    val hash: ByteArray,
)

private fun ByteArray.encodeBase64WithoutPadding(): String {
    return Base64.getEncoder().withoutPadding().encodeToString(this)
}

private fun String.decodeBase64WithoutPadding(): ByteArray? {
    val normalized = this.padEnd(this.length + ((4 - this.length % 4) % 4), '=')
    return try {
        Base64.getDecoder().decode(normalized)
    } catch (_: IllegalArgumentException) {
        null
    }
}

private const val PBKDF2_SHA256_ALGORITHM = "PBKDF2WithHmacSHA256"
private const val BITS_PER_BYTE = 8

