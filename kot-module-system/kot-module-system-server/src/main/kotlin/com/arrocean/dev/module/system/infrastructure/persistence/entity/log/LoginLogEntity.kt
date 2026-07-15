package com.arrocean.dev.module.system.infrastructure.persistence.entity.log

import com.arrocean.dev.framework.common.core.MDTenantBaseEntity
import com.arrocean.dev.framework.postgresql.convert.PostgreSqlInetColumn
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.net.InetAddress

/**
 * 登录日志 Entity
 * TODO WhiteSprite：在Java中，这些类会继承TenantBaseDO，从而天生的添加creator、createTime、updaterId、updateTime、deleted、tenantId字段
 *
 * @author WhiteSprite
 */
@MappedEntity("system_login_log")
class LoginLogEntity(

    /**
     * 主键
     */
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,

    /**
     * 日志类型
     */
    var logType: Int,

    /**
     * 链路追踪ID
     */
    var traceId: String?,

    /**
     * 用户ID
     */
    var userId: Long,

    /**
     * 用户名
     */
    var username: String,

    /**
     * 用户类型
     */
    var userType: Short,

    /**
     * 登陆结果
     */
    var result: Int,

    /**
     * 失败原因
     */
    var failReason: String? = null,

    /**
     * 邮箱
     */
    @field:PostgreSqlInetColumn
    var userIp: InetAddress? = null,

    /**
     * 浏览器UA
     */
    var userAgent: String,

    /**
     * 会话ID
     */
    var sessionId: String?

) : MDTenantBaseEntity() {
}
