package com.whitesprite.dev.module.system.application.user.command

import com.whitesprite.dev.module.system.domain.user.gateway.AdminUserGateway
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import com.whitesprite.dev.module.system.infrastructure.persistence.mapper.user.AdminUserMapper
import jakarta.inject.Singleton
import java.time.LocalDateTime

@Singleton
class UserCommandHandler(
    private val adminUserGateway: AdminUserGateway
) {
    fun handle(command: CreateUserCommand): Long {
        if (adminUserGateway.existsByUsername(command.name)) {
            throw IllegalArgumentException("用户名已存在: ${command.name}")
        }

        val saved = adminUserGateway.save(
            AdminUserEntity(
                username = command.name,
                password = command.password,
                nickname = command.name,
                remark = "testUser",
                id = 1L,
                creator = "TODO()",
                createTime = LocalDateTime.now(),
                updater = "TODO()",
                updateTime = LocalDateTime.now(),
                deleted = false
            )
        )
        return saved.id
    }

    fun handle(command: DeleteUserCommand) {
        if (!adminUserGateway.existsById(command.id)) return
        adminUserGateway.deleteById(command.id)
    }

    fun handle(command: UpdateUserCommand): AdminUser {
        val entity = adminUserGateway.findById(command.id).orElseThrow {
            NoSuchElementException("用户不存在: ${command.id}")
        }
        val updated = entity.copy(username = command.name)
        return AdminUserMapper.toDomain(adminUserGateway.update(updated))
    }
}