package com.whitesprite.dev.module.system.infrastructure.persistence.repositoryimpl.postgresql

import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.framework.core.toPageResult
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.domain.user.model.AdminUserDraft
import com.whitesprite.dev.module.system.domain.user.repository.AdminUserRepository
import com.whitesprite.dev.module.system.infrastructure.persistence.mapper.user.AdminUserMapper
import com.whitesprite.dev.module.system.infrastructure.persistence.postgresql.user.AdminUserEntityRepository
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
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
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 用户列表
     */
    override suspend fun findByPage(pageNo: Int, pageSize: Int): PageResult<AdminUser> {
        return entityRepo.findAll(Pageable.from(pageNo - 1, pageSize)).map(AdminUserMapper::toDomain).toPageResult()
    }

    /**
     * 分页列表查询用户 + 根据昵称查询
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param keyword 昵称
     * @return 用户列表
     */
    override suspend fun findByPage(pageNo: Int, pageSize: Int, keyword: String): PageResult<AdminUser> {
        // 创建分页参数；此处默认从0开始，所以pageNo减1
        val pageable = Pageable.from(pageNo - 1, pageSize)
        return entityRepo.findByNicknameIlike(keyword, pageable).map(AdminUserMapper::toDomain).toPageResult()
    }

}