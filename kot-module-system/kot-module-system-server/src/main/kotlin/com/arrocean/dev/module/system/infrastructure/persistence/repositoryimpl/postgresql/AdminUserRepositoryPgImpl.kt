package com.arrocean.dev.module.system.infrastructure.persistence.repositoryimpl.postgresql

import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.poko.PageResult
import com.arrocean.dev.framework.core.toPageResult
import com.arrocean.dev.framework.core.toPageResultTotalEqualsSize
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import com.arrocean.dev.module.system.domain.user.model.AdminUserDraft
import com.arrocean.dev.module.system.domain.user.repository.AdminUserRepository
import com.arrocean.dev.module.system.infrastructure.persistence.mapper.user.AdminUserMapper
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.user.AdminUserEntityRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

@Singleton
class AdminUserRepositoryPgImpl(
    private val entityRepo: AdminUserEntityRepository
): AdminUserRepository {

    /**
     * 保存用户/修改用户
     * @param user 用户草稿
     * @return 用户
     */
    override suspend fun save(user: AdminUserDraft): AdminUser {
        val entity = AdminUserMapper.toEntity(user)
        return AdminUserMapper.toDomain(entityRepo.save(entity))
    }

    /**
     * 删除用户
     * @param id 用户ID
     */
    override suspend fun deleteById(id: Long) {
        entityRepo.deleteById(id)
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     */
    override suspend fun batchDelete(ids: List<Long>) {
        entityRepo.deleteByIdInList(ids)
    }

    /**
     * 修改用户
     * @param user 用户
     * @return 用户
     */
    override suspend fun update(user: AdminUser): AdminUser {
        val entity = AdminUserMapper.toEntity(user)
        return AdminUserMapper.toDomain(entityRepo.save(entity))
    }

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    override suspend fun existsByUsername(username: String): Boolean {
        return entityRepo.existsByUsername(username)
    }

    /**
     * 判断用户ID是否存在
     * @param id 用户ID
     * @return 存在返回true
     */
    override suspend fun existsById(id: Long): Boolean {
        return entityRepo.existsById(id)
    }

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户
     */
    override suspend fun findById(id: Long): AdminUser? {
        return entityRepo.findById(id)?.let(AdminUserMapper::toDomain)
    }

    override suspend fun findByUsername(username: String): AdminUser? {
        return entityRepo.findByUsername(username)?.let(AdminUserMapper::toDomain)
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    override suspend fun findAll(): List<AdminUser> {
        return entityRepo.findAll().map(AdminUserMapper::toDomain).toList()
    }

    /**
     * 根据昵称模糊查询用户
     * @param name 昵称
     * @return 用户列表
     */
    override suspend fun findByNicknameIlike(name: String): List<AdminUser> {
        return entityRepo.findByNicknameIlike(name).map(AdminUserMapper::toDomain).toList()
    }

    /**
     * 分页查询所有用户
     *
     * @param page 分页参数
     * @return 用户列表
     */
    override suspend fun findByPage(page: PageParam): PageResult<AdminUser> {
        return if (page.isNoPage()) {
            entityRepo.findAll().map(AdminUserMapper::toDomain).toList().toPageResultTotalEqualsSize()
        } else {
            entityRepo.findAll(page.toPageable()).map(AdminUserMapper::toDomain).toPageResult()
        }
    }

    /**
     * 分页列表查询用户 + 根据昵称查询
     * @param page 分页参数
     * @param keyword 昵称
     * @return 用户列表
     */
    override suspend fun findByPage(page: PageParam, keyword: String): PageResult<AdminUser> {
        return if (page.isNoPage()) {
            entityRepo.findByNicknameIlike(keyword).map(AdminUserMapper::toDomain).toList().toPageResultTotalEqualsSize()
        } else {
            entityRepo.findByNicknameIlike(keyword, page.toPageable()).map(AdminUserMapper::toDomain).toPageResult()
        }
    }

}
