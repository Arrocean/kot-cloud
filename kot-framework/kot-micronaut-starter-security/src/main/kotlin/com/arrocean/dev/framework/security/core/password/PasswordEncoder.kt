package com.arrocean.dev.framework.security.core.password

/**
 * 密码编码器抽象。
 *
 * 说明：
 * - 放在 framework security starter 中，作为业务模块可复用的基础设施能力
 * - 领域层只关心 `passwordHash`，不感知具体算法
 * - 第一阶段先提供 encode / matches；后续如有需要再补 `upgradeEncoding`
 *
 * @author WhiteSprite
 */
interface PasswordEncoder {

    /**
     * 将原始密码编码为可持久化的哈希串。
     *
     * @param rawPassword 原始密码
     * @return 已编码密码（通常包含算法标识、参数、盐等信息）
     */
    fun encode(rawPassword: CharSequence): String

    /**
     * 校验原始密码是否匹配已编码密码。
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 已编码密码（通常包含算法标识、参数、盐等信息）
     * @return `true` 如果匹配，`false` 否则
     */
    fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean
}


