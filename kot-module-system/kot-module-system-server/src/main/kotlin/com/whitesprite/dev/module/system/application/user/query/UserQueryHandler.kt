package com.whitesprite.dev.module.system.application.user.query

import com.whitesprite.dev.module.system.domain.user.gateway.AdminUserGateway
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.infrastructure.persistence.mapper.user.AdminUserMapper
import jakarta.inject.Singleton

@Singleton
class UserQueryHandler(
    private val adminUserGateway: AdminUserGateway
) {
    fun handle(query: GetUserQuery): AdminUser? {
        val entity = adminUserGateway.findById(query.id).orElse(null) ?: return null
        return AdminUserMapper.toDomain(entity)
    }

    fun handle(query: ListUserQuery): List<AdminUser> {
        val all = if (query.keyword.isBlank()) {
            adminUserGateway.findAll()
        } else {
            adminUserGateway.findByNicknameIlike("%${query.keyword}%")
        }

        return all.map { AdminUserMapper.toDomain(it) }
    }
}