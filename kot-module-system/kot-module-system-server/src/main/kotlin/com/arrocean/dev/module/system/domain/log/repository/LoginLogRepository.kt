package com.arrocean.dev.module.system.domain.log.repository

import com.arrocean.dev.module.system.domain.log.model.LoginLog
import com.arrocean.dev.module.system.domain.log.model.LoginLogDraft

interface LoginLogRepository {

    /**
     * 保存登录日志。
     */
    suspend fun save(log: LoginLogDraft): LoginLog
}
