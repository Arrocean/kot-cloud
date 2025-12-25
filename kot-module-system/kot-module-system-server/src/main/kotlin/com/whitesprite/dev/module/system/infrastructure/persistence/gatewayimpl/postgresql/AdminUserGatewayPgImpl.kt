package com.whitesprite.dev.module.system.infrastructure.persistence.gatewayimpl.postgresql

import com.whitesprite.dev.module.system.domain.user.gateway.AdminUserGateway
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import com.whitesprite.dev.module.system.infrastructure.persistence.postgresql.user.AdminUserRepository
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
open class AdminUserGatewayPgImpl(
    private val adminUserRepository: AdminUserRepository
) : AdminUserGateway {

    override fun existsByUsername(name: String): Boolean {
        return adminUserRepository.existsByUsername(name)
    }

    override fun save(entity: AdminUserEntity): AdminUserEntity {
        return adminUserRepository.save(entity)
    }

    override fun existsById(id: Long): Boolean {
        return adminUserRepository.existsById(id)
    }

    override fun deleteById(id: Long) {
        adminUserRepository.deleteById(id)
    }

    @ReadOnly
    override fun findById(id: Long): Optional<AdminUserEntity> {
        return adminUserRepository.findById(id)
    }

    @Transactional
    override fun update(entity: AdminUserEntity): AdminUserEntity {
        return adminUserRepository.update(entity)
    }

    @ReadOnly
    override fun findAll(): List<AdminUserEntity> {
        return adminUserRepository.findAll()
    }

    @ReadOnly
    override fun findByNicknameIlike(name: String): List<AdminUserEntity> {
        return adminUserRepository.findByNicknameIlike(name)
    }
}