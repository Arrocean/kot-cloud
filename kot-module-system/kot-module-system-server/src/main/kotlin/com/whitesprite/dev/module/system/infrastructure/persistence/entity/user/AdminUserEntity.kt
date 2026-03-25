package com.whitesprite.dev.module.system.infrastructure.persistence.entity.user

import com.whitesprite.dev.framework.common.core.TenantBaseEntity
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

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
     * @see com.whitesprite.dev.framework.common.enums.CommonStatusEnum
     */
    var status: Short = 0,

    /**
     * 头像Url
     */
    var avatarUrl: String? = null,

    /**
     * 最后登录IP
     */
    var loginIp: String? = null,

    /**
     * 备注
     */
    var remark: String? = null,

): TenantBaseEntity() {
}