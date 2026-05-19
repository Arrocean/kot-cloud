package com.arrocean.dev.module.system.infrastructure.persistence.entity.user

import com.arrocean.dev.framework.common.core.MDTenantBaseEntity
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.net.InetAddress
import java.time.Instant

/**
 * 管理员用户 Entity
 * TODO WhiteSprite：在Java中，这些类会继承TenantBaseDO，从而天生的添加creator、createTime、updaterId、updateTime、deleted、tenantId字段
 *
 * @author WhiteSprite
 */
@MappedEntity("system_users")
data class AdminUserEntity(

    /**
     * 主键
     */
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,

    /**
     * 用户名
     */
    var username: String,

    /**
     * 加密后的密码
     * TODO WhiteSprite：将会使用自定义加密器，从而实现无需自行处理 salt
     */
    var passwordHash: String,

    /**
     * 昵称
     */
    var nickname: String,

    /**
     * 部门ID
     */
    var deptId: Long? = null,

    /**
     * 邮箱
     */
    var email: String? = null,

    /**
     * 手机号
     */
    var mobile: String? = null,

    /**
     * 性别
     */
    var gender: Short = 0,

    /**
     * 状态
     *
     * @see com.arrocean.dev.framework.common.enums.CommonStatusEnum
     */
    var status: Short = 0,

    /**
     * 头像Url
     */
    var avatarUrl: String? = null,

    /**
     * 最后登录IP
     */
    var loginIp: InetAddress? = null,

    /**
     * 最后登陆时间
     */
    var loginTime: Instant? = null,

    /**
     * 备注
     */
    var remark: String? = null,

): MDTenantBaseEntity() {
}
