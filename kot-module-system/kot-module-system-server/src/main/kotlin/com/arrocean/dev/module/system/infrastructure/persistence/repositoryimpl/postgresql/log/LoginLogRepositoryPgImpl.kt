package com.arrocean.dev.module.system.infrastructure.persistence.repositoryimpl.postgresql.log

import com.arrocean.dev.module.system.domain.log.model.LoginLog
import com.arrocean.dev.module.system.domain.log.model.LoginLogDraft
import com.arrocean.dev.module.system.domain.log.repository.LoginLogRepository
import com.arrocean.dev.module.system.infrastructure.persistence.mapper.log.LoginLogMapper
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.log.LoginLogEntityRepository
import jakarta.inject.Singleton

@Singleton
class LoginLogRepositoryPgImpl(
    private val entityRepo: LoginLogEntityRepository,
) : LoginLogRepository {

    override suspend fun save(log: LoginLogDraft): LoginLog {
        return LoginLogMapper.toDomain(entityRepo.save(LoginLogMapper.toEntity(log)))
    }
}
