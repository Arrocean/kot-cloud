package com.arrocean.dev.module.system.application.rbac.core.command

import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.MenuDraft
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.RoleDraft
import com.arrocean.dev.module.system.domain.rbac.model.UserAuthorization
import com.arrocean.dev.module.system.domain.rbac.repository.MenuRepository
import com.arrocean.dev.module.system.domain.rbac.repository.PermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RolePermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RoleRepository
import com.arrocean.dev.module.system.domain.rbac.repository.UserRoleRepository
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.net.URI

/**
 * RBAC 命令处理器，负责角色、菜单及授权关联的写入编排。
 *
 * 该处理器是 Repository 的唯一应用层写入入口；Facade 和 Controller 不直接访问 Repository。
 *
 * @author WhiteSprite
 */
@Singleton
open class RbacCommandHandler(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val menuRepository: MenuRepository,
    private val userRoleRepository: UserRoleRepository,
    private val rolePermissionRepository: RolePermissionRepository,
) {
    /**
     * 为新注册管理员绑定默认系统管理员角色。
     *
     * @param userId 新注册管理员 ID
     * @param tenantId 管理员所属租户 ID
     * @throws com.arrocean.dev.framework.common.exception.ServiceException 默认角色不存在时抛出系统配置异常
     */
    /**
     * 覆盖指定用户的角色集合。
     *
     * `super_admin` 仅允许数据库运维手工分配，业务路径禁止写入。
     *
     * @param userId 被授权用户 ID
     * @param roleIds 待分配角色 ID 集合
     * @param tenantId 租户 ID
     */
    @Transactional
    open fun assignDefaultRole(userId: Long, tenantId: Long) {
        val role = roleRepository.findByCode(DEFAULT_ROLE_CODE, tenantId)
            ?: throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.ERROR_CONFIGURATION)
        userRoleRepository.addRole(userId, role.id, tenantId)
    }

    /**
     * 覆盖角色拥有的功能权限集合。
     *
     * @param roleId 角色 ID
     * @param permissionIds 待分配权限 ID 集合
     * @param tenantId 租户 ID
     */
    @Transactional
    open fun assignUserRoles(userId: Long, roleIds: Set<Long>, tenantId: Long) {
        val roles = roleRepository.findByIds(roleIds)
        if (roles.size != roleIds.size || roles.any { it.tenantId != tenantId }) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (roles.any { it.code == UserAuthorization.SUPER_ADMIN_ROLE_CODE }) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.FORBIDDEN)
        }
        userRoleRepository.replaceRoles(userId, roleIds, tenantId)
    }

    /**
     * 创建普通可管理角色。
     *
     * @param draft 角色创建草稿
     * @return 已保存角色
     */
    @Transactional
    open fun assignRolePermissions(roleId: Long, permissionIds: Set<Long>, tenantId: Long) {
        val role = requireEditableRole(roleId, tenantId)
        val permissions = permissionRepository.findByIds(permissionIds)
        if (permissions.size != permissionIds.size || permissions.any { it.tenantId != tenantId }) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        rolePermissionRepository.replacePermissions(role.id, permissionIds, tenantId)
    }

    /**
     * 更新普通可管理角色。
     *
     * @param role 待更新角色
     * @return 更新后的角色
     */
    @Transactional
    open fun createRole(draft: RoleDraft): Role {
        if (draft.code == UserAuthorization.SUPER_ADMIN_ROLE_CODE || roleRepository.findByCode(draft.code, draft.tenantId) != null) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        return roleRepository.save(draft)
    }

    /**
     * 删除普通非内置角色。
     *
     * @param roleId 角色 ID
     * @param tenantId 租户 ID
     */
    @Transactional
    open fun updateRole(role: Role): Role {
        requireEditableRole(role.id, role.tenantId)
        return roleRepository.save(role)
    }

    /**
     * 创建菜单配置。
     *
     * @param draft 菜单创建草稿
     * @return 已保存菜单
     */
    @Transactional
    open fun deleteRole(roleId: Long, tenantId: Long) {
        val role = requireEditableRole(roleId, tenantId)
        if (role.builtIn) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.FORBIDDEN)
        }
        roleRepository.deleteById(roleId)
    }

    /**
     * 更新菜单配置。
     *
     * @param menu 待更新菜单
     * @return 更新后的菜单
     */
    @Transactional
    open fun createMenu(draft: MenuDraft): Menu {
        validateMenu(draft)
        if (menuRepository.findByCode(draft.code, draft.tenantId) != null) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        val menu = menuRepository.save(draft)
        return menu
    }

    /**
     * 删除菜单配置。
     *
     * @param menuId 菜单 ID
     * @param tenantId 租户 ID
     */
    @Transactional
    open fun updateMenu(menu: Menu): Menu {
        validateMenu(
            MenuDraft(
                code = menu.code,
                parentCode = menu.parentCode,
                title = menu.title,
                type = menu.type,
                path = menu.path,
                icon = menu.icon,
                sort = menu.sort,
                visible = menu.visible,
                permissionCode = menu.permissionCode,
                remark = menu.remark,
                tenantId = menu.tenantId,
            ),
        )
        val saved = menuRepository.save(menu)
        return saved
    }

    @Transactional
    open fun deleteMenu(menuId: Long, tenantId: Long) {
        menuRepository.findAll(tenantId).firstOrNull { it.id == menuId }
            ?: throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.NOT_FOUND)
        menuRepository.deleteById(menuId)
    }

    /**
     * 获取允许业务管理的角色。
     *
     * @param roleId 角色 ID
     * @param tenantId 租户 ID
     * @return 可编辑角色
     */
    private fun requireEditableRole(roleId: Long, tenantId: Long): Role {
        val role = roleRepository.findById(roleId)
            ?.takeIf { it.tenantId == tenantId }
            ?: throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.NOT_FOUND)
        if (role.code == UserAuthorization.SUPER_ADMIN_ROLE_CODE) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.FORBIDDEN)
        }
        return role
    }

    /**
     * 校验菜单层级和路径的基础约束。
     *
     * @param draft 待校验菜单草稿
     */
    private fun validateMenu(draft: MenuDraft) {
        if (draft.code.isBlank() || draft.code != draft.code.trim()) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.parentCode == draft.code) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.parentCode != null && menuRepository.findByCode(draft.parentCode, draft.tenantId) == null) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.type.name != "DIRECTORY" && draft.path.isNullOrBlank()) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.type.name == "DIRECTORY" && !draft.path.isNullOrBlank()) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.type.name == "INTERNAL" && draft.path?.startsWith('/') != true) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
        }
        if (draft.type.name == "EXTERNAL" || draft.type.name == "IFRAME") {
            val uri = runCatching { URI(requireNotNull(draft.path)) }.getOrNull()
            if (uri?.scheme != "https" || uri.host.isNullOrBlank()) {
                throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.BAD_REQUEST)
            }
        }
    }

    companion object {
        const val DEFAULT_ROLE_CODE = "system_admin"
    }
}
