package com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac

import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.MenuEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.PermissionEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.RoleEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.RolePermissionEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.UserRoleEntity
import io.micronaut.data.annotation.Query
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository

/**
 * 角色持久化访问接口。
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface RoleEntityRepository : CoroutineCrudRepository<RoleEntity, Long> {
    /**
     * 根据角色编码和租户查询角色。
     *
     * @param code 角色编码
     * @param tenantId 租户 ID
     * @return 角色实体；不存在时为空
     */
    suspend fun findByCodeAndTenantId(code: String, tenantId: Long): RoleEntity?

    /**
     * 根据角色 ID 集合查询角色。
     *
     * @param ids 角色 ID 集合
     * @return 角色实体列表
     */
    suspend fun findByIdInList(ids: Collection<Long>): List<RoleEntity>

    /**
     * 查询指定租户下的全部角色。
     *
     * @param tenantId 租户 ID
     * @return 按角色编码升序排列的角色实体列表
     */
    suspend fun findByTenantIdOrderByCodeAsc(tenantId: Long): List<RoleEntity>
}

/**
 * 权限持久化访问接口。
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface PermissionEntityRepository : CoroutineCrudRepository<PermissionEntity, Long> {
    /**
     * 根据权限 ID 集合查询权限。
     *
     * @param ids 权限 ID 集合
     * @return 权限实体列表
     */
    suspend fun findByIdInList(ids: Collection<Long>): List<PermissionEntity>

    /**
     * 查询指定租户下的全部权限。
     *
     * @param tenantId 租户 ID
     * @return 按权限编码升序排列的权限实体列表
     */
    suspend fun findByTenantIdOrderByCodeAsc(tenantId: Long): List<PermissionEntity>

    /**
     * 查询用户经由有效角色获得的功能权限。
     *
     * 已禁用或逻辑删除的角色、权限不会出现在结果中。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 去重后的有效权限实体列表
     */
    @Query(
        """
        SELECT DISTINCT p.*
        FROM system_permission p
        JOIN system_role_permission rp ON rp.permission_id = p.id AND rp.tenant_id = p.tenant_id
        JOIN system_user_role ur ON ur.role_id = rp.role_id AND ur.tenant_id = rp.tenant_id
        JOIN system_role r ON r.id = ur.role_id AND r.tenant_id = ur.tenant_id
        WHERE ur.user_id = :userId
          AND ur.tenant_id = :tenantId
          AND p.status = 0
          AND r.status = 0
          AND p.deleted = false
          AND r.deleted = false
        """
    )
    suspend fun findEffectiveByUserId(userId: Long, tenantId: Long): List<PermissionEntity>
}

/**
 * 菜单持久化访问接口。
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface MenuEntityRepository : CoroutineCrudRepository<MenuEntity, Long> {
    /**
     * 根据菜单编码和租户查询菜单。
     *
     * @param code 菜单编码
     * @param tenantId 租户 ID
     * @return 菜单实体；不存在时为空
     */
    suspend fun findByCodeAndTenantId(code: String, tenantId: Long): MenuEntity?

    /**
     * 查询指定租户的菜单配置。
     *
     * @param tenantId 租户 ID
     * @return 按排序号、菜单编码排列的菜单实体列表
     */
    @Query("SELECT * FROM system_menu WHERE tenant_id = :tenantId AND deleted = false ORDER BY sort , code ")
    suspend fun findAllActiveByTenantId(tenantId: Long): List<MenuEntity>
}

/**
 * 用户角色关联持久化访问接口。
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface UserRoleEntityRepository : CoroutineCrudRepository<UserRoleEntity, Long> {
    /**
     * 查询用户拥有的角色关联。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 用户角色关联列表
     */
    suspend fun findByUserIdAndTenantId(userId: Long, tenantId: Long): List<UserRoleEntity>

    /**
     * 查询拥有指定角色的用户关联。
     *
     * @param roleId 角色 ID
     * @param tenantId 租户 ID
     * @return 用户角色关联列表
     */
    suspend fun findByRoleIdAndTenantId(roleId: Long, tenantId: Long): List<UserRoleEntity>

    /**
     * 删除用户在指定租户下的全部角色关联。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 删除记录数
     */
    suspend fun deleteByUserIdAndTenantId(userId: Long, tenantId: Long): Long
}

/**
 * 角色权限关联持久化访问接口。
 *
 * @author WhiteSprite
 */
@R2dbcRepository(dialect = Dialect.POSTGRES)
interface RolePermissionEntityRepository : CoroutineCrudRepository<RolePermissionEntity, Long> {
    /**
     * 删除角色在指定租户下的全部权限关联。
     *
     * @param roleId 角色 ID
     * @param tenantId 租户 ID
     * @return 删除记录数
     */
    suspend fun deleteByRoleIdAndTenantId(roleId: Long, tenantId: Long): Long
}
