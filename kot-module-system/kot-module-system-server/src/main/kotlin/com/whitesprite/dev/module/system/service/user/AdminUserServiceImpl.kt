package com.whitesprite.dev.module.system.service.user

import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.module.system.api.user.dto.AdminUserDTO
import com.whitesprite.dev.module.system.controller.admin.user.vo.CreateUserRequestVO
import com.whitesprite.dev.module.system.controller.admin.user.vo.UpdateUserRequestVO
import com.whitesprite.dev.module.system.persistence.entity.user.AdminUserEntity
import com.whitesprite.dev.module.system.persistence.entity.user.toDTO
import com.whitesprite.dev.module.system.persistence.mariadb.user.AdminUserRepository
import jakarta.inject.Singleton
import java.time.LocalDateTime

/**
 * 用户服务实现
 */
@Singleton
open class AdminUserServiceImpl(
    private val userRepository: AdminUserRepository
): AdminUserService{
    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    override fun create(req: CreateUserRequestVO): Long {
        if (userRepository.existsByUsername(req.name)) {
            throw IllegalArgumentException("用户名已存在: ${req.name}")
        }

        val saved = userRepository.save(
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
    override fun delete(id: Long) {
        if (!userRepository.existsById(id)) return
        userRepository.deleteById(id)
    }

    /**
     * 全量更新用户
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    override fun update(
        id: Long,
        req: UpdateUserRequestVO
    ): AdminUserDTO {
        val entity = userRepository.findById(id).orElseThrow {
            NoSuchElementException("用户不存在: $id")
        }
        val updated = entity.copy(username = req.name)
        return userRepository.update(updated).toDTO()
    }

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户信息
     */
    override fun getById(id: Long): AdminUserDTO? {
        val entity = userRepository.findById(id).orElse(null) ?: return null
        return entity.toDTO()
    }

    /**
     * 列表查询用户
     * @param keyword 关键字
     * @return 用户列表
     */
    override fun list(keyword: String): List<AdminUserDTO> {
        // 简化示例：直接拉全量后内存分页（生产建议改成数据库分页）
        val all = if (keyword.isBlank()) {
            userRepository.findAll()
        } else {
            userRepository.findByNicknameIlike("%$keyword%")
        }

        return all.map { it.toDTO() }
    }

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 页大小
     * @param keyword 关键字
     * @return 用户列表
     */
    override fun page(
        page: Int,
        size: Int,
        keyword: String
    ): PageResult<AdminUserDTO> {
        TODO("Not yet implemented")
    }


}