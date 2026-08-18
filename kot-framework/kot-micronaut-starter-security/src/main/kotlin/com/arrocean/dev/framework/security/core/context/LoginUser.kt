package com.arrocean.dev.framework.security.core.context

import com.arrocean.dev.framework.common.enums.CommonUserTypeEnum
import io.micronaut.serde.annotation.Serdeable

/**
 * 登录用户信息。
 *
 * 说明：
 * - 仅保存认证后稳定且高频使用的身份字段
 * - 不使用 info/context 这类弱类型 Map，避免后续滥用与序列化边界不清
 * - 若未来确有扩展诉求，优先新增显式字段，而不是回退到万能 Map
 *
 * @author WhiteSprite
 */
@Serdeable
data class LoginUser(
    /**
     * 用户编号
     */
    val id: Long,

    /**
     * 用户类型
     */
    val userType: CommonUserTypeEnum,

    /**
     * 用户名
     */
    val username: String,

    /**
     * 用户昵称
     */
    val nickname: String,

    /**
     * 部门编号
     */
    val deptId: Long? = null,

    /**
     * 邮箱
     */
    val email: String? = null,

    /**
     * 手机号
     */
    val mobile: String? = null,

    /**
     * 租户编号
     */
    val tenantId: Long,

    /**
     * 授权范围
     */
    val scopes: Set<String> = emptySet(),

    /**
     * 会话编号
     */
    val sessionId: String? = null,
) {

    /**
     * 用户类型的数值表示，便于与 claims / 数据库存储值对接。
     */
    val userTypeValue: Int
        get() = userType.value
}

