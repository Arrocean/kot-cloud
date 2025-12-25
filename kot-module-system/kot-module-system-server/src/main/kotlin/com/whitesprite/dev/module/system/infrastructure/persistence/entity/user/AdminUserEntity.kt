package com.whitesprite.dev.module.system.infrastructure.persistence.entity.user

import com.whitesprite.dev.module.system.api.user.dto.AdminUserDto
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import java.time.LocalDateTime

/**
 * 管理员用户 Entity
 * TODO WhiteSprite：在Java中，这些类会继承TenantBaseDO，从而天生的添加creator、createTime、updater、updateTime、deleted、tenantId字段
 *
 * @author WhiteSprite
 */
data class AdminUserEntity(

    /**
     * 主键
     */
    val id: Long,

    /**
     * 用户名
     */
    val username: String,

    /**
     * 加密后的密码
     * TODO WhiteSprite：将会使用自定义加密器，从而实现无需自行处理 salt
     */
    val password: String,

    /**
     * 昵称
     */
    val nickname: String,

    /**
     * 备注
     */
    val remark: String,

    /**
     * 创建人
     */
    val creator: String,

    /**
     * 创建时间
     */
    val createTime: LocalDateTime,

    /**
     * 更新人
     */
    val updater: String,

    /**
     * 更新时间
     */
    val updateTime: LocalDateTime,

    /**
     * 删除标志
     */
    val deleted: Boolean
) {
}

/**
 * 转换为 DTO
 *
 * @return AdminUserDTO
 */
fun AdminUserEntity.toDTO(): AdminUserDto = AdminUserDto(
    id = id,
    username = username,
    password = password,
    nickname = nickname,
    remark = remark,
    creator = creator,
    createTime = createTime,
    updater = updater,
    updateTime = updateTime,
    deleted = deleted
)

/**
 * 转换为 Domain 模型
 *
 * @return AdminUser
 */
fun AdminUserEntity.toDomain(): AdminUser = AdminUser(
    id = id,
    username = username,
    password = password,
    nickname = nickname,
    remark = remark,
    creator = creator,
    createTime = createTime,
    updater = updater,
    updateTime = updateTime,
    deleted = deleted
)