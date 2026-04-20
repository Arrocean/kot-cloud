package com.whitesprite.dev.module.system.application.user.core.command

import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.security.core.context.CurrentLoginUserProvider
import com.whitesprite.dev.framework.security.core.password.PasswordEncoder
import com.whitesprite.dev.module.system.constants.user.UserErrorCodeConstants
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.domain.user.model.AdminUserDraft
import com.whitesprite.dev.module.system.domain.user.repository.AdminUserRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

@Singleton
open class UserCommandHandler(
    private val adminUserRepository: AdminUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    @Transactional
    open suspend fun handle(command: CreateUserCommand): Long? {
        if (adminUserRepository.existsByUsername(command.name)) {
            throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_USERNAME_EXISTS)
        }

        val passwordHash = passwordEncoder.encode(command.password)
        val loginUser = currentLoginUserProvider.requireLoginUser()
        val currentUserId = loginUser.id
        val tenantId = loginUser.tenantId

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
    open suspend fun handle(command: DeleteUserCommand) {
        if (!adminUserRepository.existsById(command.id)) return
        adminUserRepository.deleteById(command.id)
    }

    @Transactional
    open suspend fun handle(command: BatchDeleteUserCommand) {
        if (!command.ids.isEmpty()) {
            adminUserRepository.batchDelete(command.ids)
        }
    }

    @Transactional
    open suspend fun handle(command: UpdateUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_EXISTS)
        val updated = entity.copy(username = command.name)
        return adminUserRepository.update(updated)
    }
}