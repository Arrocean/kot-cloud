package com.whitesprite.dev.module.system.application.user

import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.module.system.adapter.web.admin.user.CreateUserRequest
import com.whitesprite.dev.module.system.adapter.web.admin.user.UpdateUserRequest
import com.whitesprite.dev.module.system.api.user.dto.AdminUserDto
import com.whitesprite.dev.module.system.domain.user.gateway.AdminUserGateway
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.toDTO
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.toDomain
import com.whitesprite.dev.module.system.infrastructure.persistence.postgresql.user.AdminUserRepository
import jakarta.inject.Singleton
import java.time.LocalDateTime

/**
 * 用户服务实现
 */
@Singleton
open class AdminUserAppService(
    private val adminUserGateway: AdminUserGateway
){
    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    fun create(req: CreateUserRequest): Long {
        if (adminUserGateway.existsByUsername(req.name)) {
            throw IllegalArgumentException("用户名已存在: ${req.name}")
        }

        val saved = adminUserGateway.save(
            AdminUserEntity(
                username = req.name,
                password = req.password,
                nickname = req.name,
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

    /**
     * 删除用户
     * @param id 用户 ID
     */
    fun delete(id: Long) {
        if (!adminUserGateway.existsById(id)) return
        adminUserGateway.deleteById(id)
    }

    /**
     * 全量更新用户
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    fun update(
        id: Long,
        req: UpdateUserRequest
    ): AdminUser {
        val entity = adminUserGateway.findById(id).orElseThrow {
            NoSuchElementException("用户不存在: $id")
        }
        val updated = entity.copy(username = req.name)
        return adminUserGateway.update(updated).toDomain()
    }

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户信息
     */
    fun getById(id: Long): AdminUser? {
        val entity = adminUserGateway.findById(id).orElse(null) ?: return null
        return entity.toDomain()
    }

    /**
     * 列表查询用户
     * @param keyword 关键字
     * @return 用户列表
     */
    fun list(keyword: String): List<AdminUser> {
        // 简化示例：直接拉全量后内存分页（生产建议改成数据库分页）
        val all = if (keyword.isBlank()) {
            adminUserGateway.findAll()
        } else {
            adminUserGateway.findByNicknameIlike("%$keyword%")
        }

        return all.map { it.toDomain() }
    }

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 页大小
     * @param keyword 关键字
     * @return 用户列表
     */
    fun page(
        page: Int,
        size: Int,
        keyword: String
    ): PageResult<AdminUserDto> {
        TODO("Not yet implemented")
    }


}