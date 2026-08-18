package com.arrocean.dev.module.system.domain.user.repository

import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.poko.PageResult
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import com.arrocean.dev.module.system.domain.user.model.AdminUserDraft

interface AdminUserRepository{

    /**
     * 保存用户/修改用户
     * @param user 用户草稿
     * @return 用户
     */
    suspend fun save(user: AdminUserDraft): AdminUser

    /**
     * 删除用户
     * @param id 用户ID
     */
    suspend fun deleteById(id: Long)

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     */
    suspend fun batchDelete(ids: List<Long>)

    /**
     * 修改用户
     * @param user 用户
     * @return 用户
     */
    suspend fun update(user: AdminUser): AdminUser

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    suspend fun existsByUsername(username: String): Boolean

    /**
     * 判断用户ID是否存在
     * @param id 用户ID
     * @return 存在返回true
     */
    suspend fun existsById(id: Long): Boolean

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户
     */
    suspend fun findById(id: Long): AdminUser?

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    suspend fun findByUsername(username: String): AdminUser?

    /**
     * 查询所有用户
     * @return 用户列表
     */
    suspend fun findAll(): List<AdminUser>

    /**
     * 根据昵称模糊查询用户
     * @param name 昵称
     * @return 用户列表
     */
    suspend fun findByNicknameIlike(name: String): List<AdminUser>

    /**
     * 分页查询所有用户
     *
     * @param page 分页参数
     * @return 用户列表
     */
    suspend fun findByPage(page: PageParam): PageResult<AdminUser>

    /**
     * 分页列表查询用户
     *
     * @param page 分页参数
     * @param keyword 关键字
     * @return 用户列表
     */
    suspend fun findByPage(page: PageParam, keyword: String): PageResult<AdminUser>

}
