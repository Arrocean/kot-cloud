package com.whitesprite.dev.module.system.infrastructure.persistence.postgresql.user

import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.kotlin.CoroutinePageableCrudRepository

/**
 * 用户数据访问接口
 *
 * 注意，此处继承 CoroutinePageableCrudRepository 为分页查询
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface AdminUserEntityRepository : CoroutinePageableCrudRepository<AdminUserEntity, Long> {

    /**
     * 判断用户名是否存在suspend fun existsByUsername(name: String): Boolean
     * TODO WhiteSprite: IDEA编译问题，导致Boolean未被正确识别，后续等待官方回应。
     *
     * @param name 用户名
     * @return 存在返回 true
     */
    suspend fun existsByUsername(name: String): Boolean

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return 用户
     */
    suspend fun findByUsername(name: String): AdminUserEntity?

    /**
     * 根据昵称模糊查询用户
     *
     * @param name 昵称
     * @return 用户列表
     */
    suspend fun findByNicknameIlike(name: String): List<AdminUserEntity>

    /**
     * 根据 Id 列表批量删除用户
     *
     * @param ids Id 列表
     * @return 删除的行数
     */
    suspend fun deleteByIdInList(ids: List<Long>)

    /**
     * 分页列表查询+根据昵称查询
     *
     * @param name 昵称
     * @param pageable 分页参数
     * @return 用户分页列表
     */
    suspend fun findByNicknameIlike(name: String, pageable: Pageable): Page<AdminUserEntity>

}