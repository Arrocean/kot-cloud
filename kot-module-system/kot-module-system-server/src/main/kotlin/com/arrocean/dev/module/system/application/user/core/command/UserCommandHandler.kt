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

/**
 * 用户命令处理器，负责编排用户创建、删除、更新、注册和登录信息更新操作。
 */
@Singleton
open class UserCommandHandler(
    private val adminUserRepository: AdminUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    /**
     * 处理创建用户命令。
     *
     * @param command 包含用户名和密码的[CreateUserCommand]对象
     * @return 创建成功后的用户ID；未生成ID时返回null
     */
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
            remark = "testUser"
        )

        val saved = adminUserRepository.save(draft)
        return saved.id
    }

    /**
     * 处理删除用户命令。
     *
     * 用户不存在时不会执行删除操作。
     *
     * @param command 包含待删除用户ID的[DeleteUserCommand]对象
     */
    @Transactional
    open suspend fun handle(command: DeleteUserCommand) {
        if (!adminUserRepository.existsById(command.id)) return
        adminUserRepository.deleteById(command.id)
    }

    /**
     * 处理批量删除用户命令。
     *
     * 用户ID列表为空时不会执行删除操作。
     *
     * @param command 包含待删除用户ID列表的[BatchDeleteUserCommand]对象
     */
    @Transactional
    open suspend fun handle(command: BatchDeleteUserCommand) {
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
    open suspend fun handle(command: UpdateUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_EXISTS)
        val updated = entity.copy(username = command.name)
        return adminUserRepository.update(updated)
    }

    /**
     * 处理用户注册命令。
     *
     * @param command 包含用户名、密码和昵称的[RegisterUserCommand]对象
     * @return 创建成功后的[AdminUser]对象
     */
    @Transactional
    open suspend fun handle(command: RegisterUserCommand): AdminUser {
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
    open suspend fun handle(command: LoginUserCommand): AdminUser {
        val entity = adminUserRepository.findById(command.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_EXISTS)
        val updated = entity.copy(
            loginIp = command.loginIp,
            loginTime = command.loginTime
        )
        return adminUserRepository.update(updated)
    }
}
