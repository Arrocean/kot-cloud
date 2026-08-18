package com.arrocean.dev.module.system.application.rbac.core.query

import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.UserAuthorization
import com.arrocean.dev.module.system.domain.rbac.repository.MenuRepository
import com.arrocean.dev.module.system.domain.rbac.repository.PermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RoleRepository
import com.arrocean.dev.module.system.domain.rbac.repository.UserRoleRepository
import jakarta.inject.Singleton

/**
 * RBAC 查询处理器，负责读取角色、权限、菜单和用户角色关联。
 *
 * @author WhiteSprite
 */
@Singleton
open class RbacQueryHandler(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val menuRepository: MenuRepository,
    private val userRoleRepository: UserRoleRepository,
) {
    /**
     * 查询可在业务界面管理的角色。
     *
     * @param tenantId 租户 ID
     * @return 已排除 `super_admin` 的角色列表
     */
    open suspend fun listEditableRoles(tenantId: Long): List<Role> = roleRepository.findAll(tenantId)
        .filterNot { it.code == UserAuthorization.SUPER_ADMIN_ROLE_CODE }

    /**
     * 查询业务可编辑的角色。
     *
     * @param id 角色 ID
     * @param tenantId 租户 ID
     * @return 角色；不存在、跨租户或为 `super_admin` 时为空
     */
    open suspend fun getRole(id: Long, tenantId: Long): Role? = roleRepository.findById(id)
        ?.takeIf { it.tenantId == tenantId && it.code != UserAuthorization.SUPER_ADMIN_ROLE_CODE }

    /**
     * 查询租户可用权限。
     *
     * @param tenantId 租户 ID
     * @return 权限列表
     */
    open suspend fun listPermissions(tenantId: Long): List<Permission> = permissionRepository.findAll(tenantId)

    /**
     * 查询租户菜单配置。
     *
     * @param tenantId 租户 ID
     * @return 菜单列表
     */
    open suspend fun listMenus(tenantId: Long): List<Menu> = menuRepository.findAll(tenantId)

    /**
     * 根据 ID 查询菜单。
     *
     * @param id 菜单 ID
     * @param tenantId 租户 ID
     * @return 菜单；不存在时为空
     */
    open suspend fun getMenu(id: Long, tenantId: Long): Menu? = menuRepository.findAll(tenantId).firstOrNull { it.id == id }

    /**
     * 查询用户当前角色 ID 集合。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 角色 ID 集合
     */
    open suspend fun getUserRoleIds(userId: Long, tenantId: Long): Set<Long> =
        userRoleRepository.findRoleIdsByUserId(userId, tenantId)
}
