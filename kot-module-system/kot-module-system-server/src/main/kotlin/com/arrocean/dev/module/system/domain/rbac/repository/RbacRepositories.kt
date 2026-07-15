package com.arrocean.dev.module.system.domain.rbac.repository

import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.MenuDraft
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.RoleDraft

/**
 * 角色领域仓储。
 *
 * @author WhiteSprite
 */
interface RoleRepository {
    /** @param id 角色 ID @return 角色；不存在时为空 */
    fun findById(id: Long): Role?
    /** @param code 角色编码 @param tenantId 租户 ID @return 角色；不存在时为空 */
    fun findByCode(code: String, tenantId: Long): Role?
    /** @param ids 角色 ID 集合 @return 已存在角色列表 */
    fun findByIds(ids: Collection<Long>): List<Role>
    /** @param tenantId 租户 ID @return 租户内角色列表 */
    fun findAll(tenantId: Long): List<Role>
    /** @param draft 角色创建草稿 @return 已保存角色 */
    fun save(draft: RoleDraft): Role
    /** @param role 待更新角色 @return 更新后的角色 */
    fun save(role: Role): Role
    /** @param id 待删除角色 ID */
    fun deleteById(id: Long)
}

/**
 * 功能权限领域仓储。
 *
 * @author WhiteSprite
 */
interface PermissionRepository {
    /** @param tenantId 租户 ID @return 租户内权限列表 */
    fun findAll(tenantId: Long): List<Permission>
    /** @param ids 权限 ID 集合 @return 已存在权限列表 */
    fun findByIds(ids: Collection<Long>): List<Permission>
    /** @param userId 用户 ID @param tenantId 租户 ID @return 用户经由角色获得的有效权限 */
    fun findEffectiveByUserId(userId: Long, tenantId: Long): List<Permission>
}

/**
 * 菜单领域仓储。
 *
 * @author WhiteSprite
 */
interface MenuRepository {
    /** @param tenantId 租户 ID @return 菜单列表 */
    fun findAll(tenantId: Long): List<Menu>
    /** @param code 菜单编码 @param tenantId 租户 ID @return 菜单；不存在时为空 */
    fun findByCode(code: String, tenantId: Long): Menu?
    /** @param draft 菜单创建草稿 @return 已保存菜单 */
    fun save(draft: MenuDraft): Menu
    /** @param menu 待更新菜单 @return 更新后的菜单 */
    fun save(menu: Menu): Menu
    /** @param id 待删除菜单 ID */
    fun deleteById(id: Long)
}

/**
 * 用户角色关联领域仓储。
 *
 * @author WhiteSprite
 */
interface UserRoleRepository {
    /** @param userId 用户 ID @param tenantId 租户 ID @return 用户角色 ID 集合 */
    fun findRoleIdsByUserId(userId: Long, tenantId: Long): Set<Long>
    /** @param roleId 角色 ID @param tenantId 租户 ID @return 拥有角色的用户 ID 集合 */
    fun findUserIdsByRoleId(roleId: Long, tenantId: Long): Set<Long>
    /** @param userId 用户 ID @param roleIds 角色 ID 集合 @param tenantId 租户 ID */
    fun replaceRoles(userId: Long, roleIds: Set<Long>, tenantId: Long)
    /** @param userId 用户 ID @param roleId 角色 ID @param tenantId 租户 ID */
    fun addRole(userId: Long, roleId: Long, tenantId: Long)
}

/**
 * 角色权限关联领域仓储。
 *
 * @author WhiteSprite
 */
interface RolePermissionRepository {
    /** @param roleId 角色 ID @param permissionIds 权限 ID 集合 @param tenantId 租户 ID */
    fun replacePermissions(roleId: Long, permissionIds: Set<Long>, tenantId: Long)
}
