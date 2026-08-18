package com.arrocean.dev.framework.security.core.password

import com.arrocean.dev.framework.security.config.SecurityProperties
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

/**
 * 基于 Bouncy Castle 的 Argon2id 密码编码器。
 *
 * 说明：
 * - 使用纯 Java 实现，更利于后续原生镜像场景
 * - 输出格式遵循 PHC 风格，便于后续迁移与排查
 * - 当前不额外启用 secret / associatedData，先满足后台账号密码场景
 *
 * 例：
 * `$argon2id$v=19$m=65536,t=3,p=1$<salt>$<hash>`
 *
 * @author WhiteSprite
 */
open class Argon2idPasswordEncoder(
    private val properties: SecurityProperties.Argon2idProperties,
    private val secureRandom: SecureRandom,
) : PasswordEncoder {

    override fun encode(rawPassword: CharSequence): String {
        validateProperties(properties)

        val salt = ByteArray(properties.saltLength)
        secureRandom.nextBytes(salt)

        val hash = hash(rawPassword, salt, properties)
        return buildEncodedPassword(properties, salt, hash)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        val parsed = parseEncodedPassword(encodedPassword) ?: return false
        val actualHash = hash(rawPassword, parsed.salt, parsed.parameters)
        return MessageDigest.isEqual(parsed.hash, actualHash)
    }

    private fun hash(
        rawPassword: CharSequence,
        salt: ByteArray,
        properties: SecurityProperties.Argon2idProperties,
    ): ByteArray {
        val parameters = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(properties.iterations)
            .withMemoryAsKB(properties.memoryKb)
            .withParallelism(properties.parallelism)
            .withSalt(salt)
            .build()

        val generator = Argon2BytesGenerator()
        generator.init(parameters)

        val output = ByteArray(properties.hashLength)
        generator.generateBytes(rawPassword.toString().toByteArray(Charsets.UTF_8), output)
        return output
    }

    private fun buildEncodedPassword(
        properties: SecurityProperties.Argon2idProperties,
        salt: ByteArray,
        hash: ByteArray,
    ): String {
        val saltValue = salt.encodeBase64WithoutPadding()
        val hashValue = hash.encodeBase64WithoutPadding()
        return "\$argon2id\$v=19\$m=${properties.memoryKb},t=${properties.iterations},p=${properties.parallelism}\$$saltValue\$$hashValue"
    }

    private fun parseEncodedPassword(encodedPassword: String): ParsedArgon2idPassword? {
        val parts = encodedPassword.split('$')
        if (parts.size != 6) {
            return null
        }
        if (parts[1] != "argon2id") {
            return null
        }
        if (parts[2] != "v=19") {
            return null
        }

        val parameterMap = parts[3]
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

        val memoryKb = parameterMap["m"]?.toIntOrNull() ?: return null
        val iterations = parameterMap["t"]?.toIntOrNull() ?: return null
        val parallelism = parameterMap["p"]?.toIntOrNull() ?: return null
        val salt = parts[4].decodeBase64WithoutPadding() ?: return null
        val hash = parts[5].decodeBase64WithoutPadding() ?: return null

        val parsedProperties = SecurityProperties.Argon2idProperties().apply {
            this.memoryKb = memoryKb
            this.iterations = iterations
            this.parallelism = parallelism
            this.saltLength = salt.size
            this.hashLength = hash.size
        }
        validateProperties(parsedProperties)

        return ParsedArgon2idPassword(
            parameters = parsedProperties,
            salt = salt,
            hash = hash,
        )
    }

    private fun validateProperties(properties: SecurityProperties.Argon2idProperties) {
        require(properties.saltLength >= 8) { "Argon2id saltLength 不能小于 8" }
        require(properties.hashLength >= 16) { "Argon2id hashLength 不能小于 16" }
        require(properties.memoryKb > 0) { "Argon2id memoryKb 必须大于 0" }
        require(properties.iterations > 0) { "Argon2id iterations 必须大于 0" }
        require(properties.parallelism > 0) { "Argon2id parallelism 必须大于 0" }
    }
}

private data class ParsedArgon2idPassword(
    val parameters: SecurityProperties.Argon2idProperties,
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


