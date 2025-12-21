package com.whitesprite.dev.module.system.api.user.dto

import java.time.LocalDateTime

data class AdminUserDTO(

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
)