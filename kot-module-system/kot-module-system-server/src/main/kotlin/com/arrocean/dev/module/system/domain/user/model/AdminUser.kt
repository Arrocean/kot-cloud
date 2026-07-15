package com.arrocean.dev.module.system.domain.user.model

import java.net.InetAddress
import java.time.Instant

/**
 * 系统用户领域模型
 * PS：聚合根
 *
 * @author WhiteSprite
 */
data class AdminUser(
    val id: Long,
    var username: String,
    var passwordHash: String,
    var nickname: String,
    var deptId: Long?,
    var email: String?,
    var mobile: String?,
    var gender: Short,
    var status: Short,
    var avatarUrl: String?,
    var loginIp: InetAddress?,
    var loginTime: Instant?,
    var remark: String?,
    var creatorId: Long?,
    var createTime: Instant,
    var updaterId: Long? ,
    var updateTime: Instant,
    var deleted: Boolean = false,
    var tenantId: Long = 0
)

/**
 * 创建阶段的系统用户草稿
 */
data class AdminUserDraft(
    val username: String,
    val passwordHash: String,
    val nickname: String,
    val deptId: Long?,
    val email: String?,
    val mobile: String?,
    val gender: Short,
    val status: Short,
    val avatarUrl: String?,
    val loginIp: InetAddress?,
    val remark: String?
)
