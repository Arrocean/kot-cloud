package com.arrocean.dev.module.system.application.user.core.facade

import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.poko.PageResult
import com.arrocean.dev.module.system.adapter.web.admin.user.CreateUserRequest
import com.arrocean.dev.module.system.adapter.web.admin.user.UpdateUserRequest
import com.arrocean.dev.module.system.application.user.core.command.*
import com.arrocean.dev.module.system.application.user.core.query.GetUserByUsernameQuery
import com.arrocean.dev.module.system.application.user.core.query.GetUserQuery
import com.arrocean.dev.module.system.application.user.core.query.ListUserQuery
import com.arrocean.dev.module.system.application.user.core.query.PageListUserQuery
import com.arrocean.dev.module.system.application.user.core.query.UserQueryHandler
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Singleton

/**
 * 用户服务实现
 */
@Singleton
open class AdminUserAppService(
    private val commandHandler: UserCommandHandler,
    private val queryHandler: UserQueryHandler
) {

    private val log = KotlinLogging.logger {}

    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    suspend fun create(req: CreateUserRequest): Long? {
        val cmd = CreateUserCommand.fromRequest(req)
        return commandHandler.handle(cmd)
    }

    /**
     * 批量创建用户
     */
    fun batchCreate(reqs: List<CreateUserRequest>): List<Long> {
        if (reqs.isEmpty()) {
            return emptyList()
        }
        throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.NOT_IMPLEMENTED)
    }

    /**
     * 删除用户
     * @param id 用户 ID
     */
    suspend fun delete(id: Long) {
        commandHandler.handle(DeleteUserCommand.fromId(id))
    }

    /**
     * 批量删除用户
     */
    suspend fun batchDelete(ids: List<Long>) {
        commandHandler.handle(BatchDeleteUserCommand.fromIds(ids))
    }

    /**
     * 全量更新用户
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    suspend fun update(
        id: Long,
        req: UpdateUserRequest
    ): AdminUser {
        val cmd = UpdateUserCommand.fromRequest(id, req)
        return commandHandler.handle(cmd)
    }

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户信息
     */
    suspend fun getById(id: Long): AdminUser? {
        return queryHandler.handle(GetUserQuery.fromId(id))
    }

    /**
     * 根据用户名查询用户
     */
    suspend fun getByUsername(username: String): AdminUser? {
        return queryHandler.handle(GetUserByUsernameQuery(username))
    }

    /**
     * 列表查询用户
     * @param keyword 关键字
     * @return 用户列表
     */
    suspend fun list(keyword: String): List<AdminUser> {
        return queryHandler.handle(ListUserQuery.fromKeyword(keyword))
    }

    /**
     * 分页查询用户
     * @param page 分页参数
     * @param keyword 关键字
     * @return 用户列表
     */
    suspend fun page(
        page: PageParam,
        keyword: String?
    ): PageResult<AdminUser> {
        return queryHandler.handle(PageListUserQuery.fromPage(page, keyword))
    }

    /**
     * 测试日志系统
     */
    fun testLogging() {
        log.trace { "LOGGING-TEST trace" }
        log.debug { "LOGGING-TEST debug" }
        log.info { "LOGGING-TEST info" }
        log.warn { "LOGGING-TEST warn" }
        log.error { "LOGGING-TEST error" }
    }

}