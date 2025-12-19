package com.whitesprite.dev.module.system.dal.dataobject.user

import java.time.LocalDateTime

/**
 * 管理员用户 DO
 * TODO WhiteSprite：在Java中，这些类会继承TenantBaseDO，从而天生的添加creator、createTime、updater、updateTime、deleted、tenantId字段
 *
 * @author WhiteSprite
 */
data class AdminUserDO (
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