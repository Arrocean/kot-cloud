package com.arrocean.dev.module.system.application.user.core.command

import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.framework.security.core.password.PasswordEncoder
import com.arrocean.dev.module.system.constants.user.UserErrorCodeConstants
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import com.arrocean.dev.module.system.domain.user.model.AdminUserDraft
import com.arrocean.dev.module.system.domain.user.repository.AdminUserRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

@Singleton
open class UserCommandHandler(
    private val adminUserRepository: AdminUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    @Transactional
    open fun handle(command: CreateUserCommand): Long? {
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
            remark = "testUser"
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

    /**
     * 处理更新用户命令。
     *
     * @param command 包含待更新用户的ID和新用户名的[UpdateUserCommand]对象
     * @return 更新后的[AdminUser]对象
     */
    @Transactional
    open fun handle(command: UpdateUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_EXISTS)
        val updated = entity.copy(username = command.name)
        return adminUserRepository.update(updated)
    }

    @Transactional
    open fun handle(command: RegisterUserCommand): AdminUser {
        if (adminUserRepository.existsByUsername(command.username)) {
            throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_USERNAME_EXISTS)
        }
        val passwordHash = passwordEncoder.encode(command.password)
        val draft = AdminUserDraft(
            username = command.username,
            passwordHash = passwordHash,
            nickname = command.nickname,
            deptId = null,
            email = null,
            mobile = null,
            gender = 0,
            status = 1,
            avatarUrl = null,
            loginIp = null,
            remark = null,
        )
        return adminUserRepository.save(draft)
    }

    /**
     * 处理用户登录更新命令
     *
     * @param command 包含待更新用户的ID和新用户信息的[LoginUserCommand]对象
     * @return 更新后的[AdminUser]对象
     */
    @Transactional
    open fun handle(command: LoginUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_EXISTS)
        val updated = entity.copy(
            loginIp = command.loginIp,
            loginTime = command.loginTime
        )
        return adminUserRepository.update(updated)
    }
}
