package com.whitesprite.dev.module.system.application.user.core.query

import com.whitesprite.dev.framework.common.exception.util.ServiceExceptionFactory
import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.module.system.constants.user.UserErrorCodeConstants
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.domain.user.repository.AdminUserRepository
import io.micronaut.transaction.annotation.ReadOnly
import jakarta.inject.Singleton

@Singleton
open class UserQueryHandler(
    private val adminUserRepository: AdminUserRepository
) {
    /**
     * 根据用户ID查询用户
     *
     * @param query 查询参数
     * @return 用户
     */
    @ReadOnly
    open fun handle(query: GetUserQuery): AdminUser? {
        return adminUserRepository.findById(query.id)
            ?: throw ServiceExceptionFactory.exception(UserErrorCodeConstants.USER_NOT_FOUND)
    }

    /**
     * 列表查询用户
     *
     * @param query 查询参数
     * @return 用户列表
     */
    @ReadOnly
    open fun handle(query: ListUserQuery): List<AdminUser> {
        val all = if (query.keyword.isBlank()) {
            adminUserRepository.findAll()
        } else {
            adminUserRepository.findByNicknameIlike("%${query.keyword}%")
        }

        return all.map { it }
    }

    /**
     * 分页列表查询用户
     *
     * @param query 获取用户请求
     * @return 用户
     */
    @ReadOnly
    open fun handle(query: PageListUserQuery): PageResult<AdminUser> {
        return if (query.keyword.isBlank()) {
            adminUserRepository.findByPage(query.pageNo, query.pageSize)
        } else {
            adminUserRepository.findByPage(query.pageNo, query.pageSize, "%${query.keyword}%")
        }
    }
}