package com.whitesprite.dev.module.system.domain.user.gateway

import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import java.util.Optional

interface AdminUserGateway {
    /**
     * 确认用户名是否存在
     * @param name 用户名
     * @return 用户名是否存在的结果
     */
    fun existsByUsername(name: String): Boolean

    /**
     * 保存用户信息
     * @param entity 用户信息
     * @return 保存后的用户信息
     */
    fun save(entity: AdminUserEntity): AdminUserEntity

    /**
     * 确认用户ID是否存在
     * @param id 用户ID
     * @return 用户ID是否存在的结果
     */
    fun existsById(id: Long): Boolean

    /**
     * 删除用户信息
     * @param id 用户ID
     */
    fun deleteById(id: Long)

    /**
     * 查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    fun findById(id: Long): Optional<AdminUserEntity>

    /**
     * 更新用户信息
     */
    fun update(entity: AdminUserEntity): AdminUserEntity

    /**
     * 查询所有用户信息
     */
    fun findAll(): List<AdminUserEntity>

    /**
     * 根据昵称模糊查询用户信息
     */
    fun findByNicknameIlike(name: String): List<AdminUserEntity>
}