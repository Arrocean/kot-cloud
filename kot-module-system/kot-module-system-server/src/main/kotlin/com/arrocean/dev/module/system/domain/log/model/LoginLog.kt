package com.arrocean.dev.module.system.domain.log.model

import java.net.InetAddress
import java.time.Instant

/**
 * 登录日志领域模型。
 */
data class LoginLog(
    val id: Long,
    var logType: Int,
//    var traceId: String,
    var userId: Long,
    var username: String,
    var userType: Short,
    var result: Int,
    var failReason: String? = null,
    var userIp: InetAddress? = null,
    var userAgent: String,
    var sessionId: String? = null,
    var creatorId: Long? = null,
    var createTime: Instant,
    var updaterId: Long? = null,
    var updateTime: Instant,
    var deleted: Boolean = false,
    var tenantId: Long = 0,
)

/**
 * 创建阶段的登录日志草稿。
 */
data class LoginLogDraft(
    val logType: Int,
//    val traceId: String,
    val userId: Long,
    val username: String,
    val userType: Short,
    val result: Int,
    val failReason: String? = null,
    val userIp: InetAddress? = null,
    val userAgent: String,
    val sessionId: String? = null,
)

