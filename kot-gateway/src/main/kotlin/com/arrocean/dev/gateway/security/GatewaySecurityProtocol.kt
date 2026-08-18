package com.arrocean.dev.gateway.security

/** 从 Authorization 请求头提取 Bearer Token。 */
object BearerTokenReader {

    /**
     * 从 Authorization 值提取不含认证方案前缀的 Bearer Token。
     *
     * @param authorization 原始 Authorization 请求头，可为空
     * @return 格式合法且非空的 Token；不是 Bearer 认证时返回 null
     */
    fun extract(authorization: String?): String? {
        val value = authorization?.trim() ?: return null
        if (!value.startsWith("Bearer ", ignoreCase = true)) {
            return null
        }
        return value.substringAfter(' ').trim().takeIf(String::isNotBlank)
    }
}

/** 统一构造 Redis 中的登录会话 Key。 */
object GatewaySessionKeyFactory {

    /**
     * 构造与下游安全模块一致的 Redis 会话记录 Key。
     *
     * @param keyPrefix Redis Key 前缀
     * @param sessionId JWT 中的会话标识
     * @return 会话记录的完整 Redis Key
     * @throws IllegalArgumentException 前缀或会话标识为空时抛出
     */
    fun sessionKey(keyPrefix: String, sessionId: String): String {
        require(keyPrefix.isNotBlank()) { "Redis key prefix must not be blank" }
        require(sessionId.isNotBlank()) { "Session id must not be blank" }
        return "${keyPrefix.trimEnd(':')}:session:${sessionId.trim()}"
    }
}

/**
 * 匹配无需网关认证的路径规则。
 *
 * 支持精确路径及以双星号结尾的子路径通配规则。
 */
class PublicPathMatcher(patterns: List<String>) {

    private val patterns = patterns.map(String::trim).filter(String::isNotBlank)

    /**
     * 判断请求路径是否符合任一匿名路径规则。
     *
     * @param path 请求路径，不要求预先带有前导斜杠
     * @return 命中精确规则或子路径通配规则时返回 true
     */
    fun matches(path: String): Boolean {
        val normalizedPath = path.ensureLeadingSlash()
        return patterns.any { pattern ->
            when {
                pattern.endsWith("/**") -> {
                    val prefix = pattern.removeSuffix("/**").ensureLeadingSlash().trimEnd('/')
                    normalizedPath == prefix || normalizedPath.startsWith("$prefix/")
                }
                else -> normalizedPath == pattern.ensureLeadingSlash()
            }
        }
    }

    private fun String.ensureLeadingSlash(): String = if (startsWith('/')) this else "/$this"
}
