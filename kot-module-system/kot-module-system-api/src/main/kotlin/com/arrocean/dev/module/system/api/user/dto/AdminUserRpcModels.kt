package com.arrocean.dev.module.system.api.user.dto

import java.net.InetAddress
import java.time.Instant
import java.time.LocalDateTime

data class AdminUserDto(

    /**
     * 主键
     */
    var id: Long?,

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
    var deptId: Long?,

    /**
     * 邮箱
     */
    var email: String?,

    /**
     * 手机号
     */
    var mobile: String?,

    /**
     * 性别
     */
    var gender: Short,

    /**
     * 状态
     */
    var status: Short,

    /**
     * 头像Url
     */
    var avatarUrl: String?,

    /**
     * 最后登录IP
     */
    var loginIp: InetAddress?,

    /**
     * 最后登录时间
     */
    var loginTime: Instant?,

    /**
     * 备注
     */
    var remark: String?,

    /**
     * 创建人
     */
    var creatorId: Long?,

    /**
     * 创建时间
     */
    var createTime: Instant = Instant.now(),

    /**
     * 更新人
     */
    var updaterId: Long?,

    /**
     * 更新时间
     */
    var updateTime: Instant = Instant.now(),

    /**
     * 删除标志
     */
    var deleted: Boolean,

    /**
     * 租户ID
     */
    var tenantId: Long = 0
)


