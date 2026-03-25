package com.whitesprite.dev.module.system.infrastructure.persistence.postgresql.user

import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

/**
 * 用户数据访问接口
 *
 * 注意，此处继承 PageableRepository 为分页查询
 *
 * @author WhiteSprite
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AdminUserEntityRepository : PageableRepository<AdminUserEntity, Long> {

    /**
     * 判断用户名是否存在
     *
     * @param name 用户名
     * @return 存在返回 true
     */
    fun existsByUsername(name: String): Boolean

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return 用户
     */
    fun findByUsername(name: String): List<AdminUserEntity>

    /**
     * 根据昵称模糊查询用户
     *
     * @param name 昵称
     * @return 用户列表
     */
    fun findByNicknameIlike(name: String): List<AdminUserEntity>

    /**
     * 根据 Id 列表批量删除用户
     *
     * @param ids Id 列表
     * @return 删除的行数
     */
    fun deleteByIdInList(ids: List<Long>)

    /**
     * 分页列表查询+根据昵称查询
     *
     * @param name 昵称
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    fun findByNicknameIlike(name: String, pageable: Pageable): Page<AdminUserEntity>

}