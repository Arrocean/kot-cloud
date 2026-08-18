package com.arrocean.dev.module.system.infrastructure.persistence.mapper.log

import com.arrocean.dev.module.system.domain.log.model.LoginLog
import com.arrocean.dev.module.system.domain.log.model.LoginLogDraft
import com.arrocean.dev.module.system.infrastructure.persistence.entity.log.LoginLogEntity

/**
 * 登录日志映射器。
 */
object LoginLogMapper {

    fun toDomain(entity: LoginLogEntity): LoginLog {
        val id = requireNotNull(entity.id) {
            "LoginLogEntity.id 为空, 实例=$entity"
        }
        val createTime = requireNotNull(entity.createTime) {
            "LoginLogEntity.createTime 为空, id=$id"
        }
        val updateTime = requireNotNull(entity.updateTime) {
            "LoginLogEntity.updateTime 为空, id=$id"
        }
        return LoginLog(
            id = id,
            logType = entity.logType,
//            traceId = entity.traceId,
            userId = entity.userId,
            username = entity.username,
            userType = entity.userType,
            result = entity.result,
            failReason = entity.failReason,
            userIp = entity.userIp,
            userAgent = entity.userAgent,
            sessionId = entity.sessionId,
            creatorId = entity.creatorId,
            createTime = createTime,
            updaterId = entity.updaterId,
            updateTime = updateTime,
            deleted = entity.deleted,
            tenantId = entity.tenantId,
        )
    }

    fun toEntity(domain: LoginLogDraft): LoginLogEntity {
        return LoginLogEntity(
            logType = domain.logType,
//            traceId = domain.traceId,
            traceId = null,
            userId = domain.userId,
            username = domain.username,
            userType = domain.userType,
            result = domain.result,
            failReason = domain.failReason,
            userIp = domain.userIp,
            userAgent = domain.userAgent,
            sessionId = domain.sessionId,
        )
    }
}

