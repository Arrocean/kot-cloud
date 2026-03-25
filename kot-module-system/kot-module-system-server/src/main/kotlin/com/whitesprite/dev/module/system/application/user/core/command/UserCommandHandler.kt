package com.whitesprite.dev.module.system.application.user.core.command

import com.whitesprite.dev.framework.security.core.password.PasswordEncoder
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.domain.user.model.AdminUserDraft
import com.whitesprite.dev.module.system.domain.user.repository.AdminUserRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

@Singleton
open class UserCommandHandler(
    private val adminUserRepository: AdminUserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    open fun handle(command: CreateUserCommand): Long? {
        if (adminUserRepository.existsByUsername(command.name)) {
            throw IllegalArgumentException("用户名已存在: ${command.name}")
        }

        val passwordHash = passwordEncoder.encode(command.password)

        // 这里先用占位值，后面你接入安全框架/JWT 后改为从上下文取
        val currentUserId = 0L
        val tenantId = 1L

        val draft = AdminUserDraft(
            username = command.name,
            passwordHash = passwordHash,
            nickname = command.name,
            deptId = null,
            email = null,
            mobile = null,
            gender = 0,
            status = 0,
            avatarUrl = null,
            loginIp = null,
            remark = "testUser",
            creatorId = currentUserId,
            tenantId = tenantId
        )

        val saved = adminUserRepository.save(draft)
        return saved.id
    }

    @Transactional
    open fun handle(command: DeleteUserCommand) {
        if (!adminUserRepository.existsById(command.id)) return
        adminUserRepository.deleteById(command.id)
    }

    @Transactional
    open fun handle(command: BatchDeleteUserCommand) {
        if (!command.ids.isEmpty()) {
            adminUserRepository.batchDelete(command.ids)
        }
    }

    @Transactional
    open fun handle(command: UpdateUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id) ?: throw NoSuchElementException("用户不存在: ${command.id}")
        val updated = entity.copy(username = command.name)
        return adminUserRepository.update(updated)
    }
}