package com.arrocean.dev.module.system.application.log.core.facade

import com.arrocean.dev.module.system.domain.log.model.LoginLog
import com.arrocean.dev.module.system.domain.log.model.LoginLogDraft
import com.arrocean.dev.module.system.domain.log.repository.LoginLogRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

/**
 * 登录日志应用服务。
 */
@Singleton
open class LoginLogService(
    private val loginLogRepository: LoginLogRepository,
) {

    @Transactional
    open fun createLoginLog(log: LoginLogDraft): LoginLog {
        return loginLogRepository.save(log)
    }
}
