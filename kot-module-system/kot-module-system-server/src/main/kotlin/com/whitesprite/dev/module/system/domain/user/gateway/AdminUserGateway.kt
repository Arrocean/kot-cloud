package com.whitesprite.dev.module.system.domain.user.gateway

import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import java.util.Optional

interface AdminUserGateway {
    fun existsByUsername(name: String): Boolean

    fun save(entity: AdminUserEntity): AdminUserEntity

    fun existsById(id: Long): Boolean

    fun deleteById(id: Long)

    fun findById(id: Long): Optional<AdminUserEntity>

    fun update(entity: AdminUserEntity): AdminUserEntity

    fun findAll(): List<AdminUserEntity>

    fun findByNicknameIlike(name: String): List<AdminUserEntity>
}