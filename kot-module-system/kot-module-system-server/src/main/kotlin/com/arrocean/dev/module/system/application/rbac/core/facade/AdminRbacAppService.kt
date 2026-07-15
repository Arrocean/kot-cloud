package com.arrocean.dev.module.system.application.rbac.core.facade

import com.arrocean.dev.module.system.application.rbac.core.command.RbacCommandHandler
import com.arrocean.dev.module.system.application.rbac.core.query.RbacQueryHandler
import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.module.system.domain.rbac.model.DataScopeType
import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.MenuDraft
import com.arrocean.dev.module.system.domain.rbac.model.MenuType
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.RoleDraft
import jakarta.inject.Singleton

/**
 * 管理端 RBAC 应用服务，负责将 Web 用例编排到命令和查询处理器。
 *
 * 该服务不直接依赖任何 Repository。
 *
 * @author WhiteSprite
 */
@Singleton
open class AdminRbacAppService(
    private val commandHandler: RbacCommandHandler,
    private val queryHandler: RbacQueryHandler,
) {
    /** @param tenantId 租户 ID @return 可管理角色列表 */
    open fun listRoles(tenantId: Long): List<Role> = queryHandler.listEditableRoles(tenantId)
    /** @param tenantId 租户 ID @return 权限列表 */
    open fun listPermissions(tenantId: Long): List<Permission> = queryHandler.listPermissions(tenantId)
    /** @param tenantId 租户 ID @return 菜单配置列表 */
    open fun listMenus(tenantId: Long): List<Menu> = queryHandler.listMenus(tenantId)
    /** @param userId 用户 ID @param tenantId 租户 ID @return 用户角色 ID 集合 */
    open fun getUserRoleIds(userId: Long, tenantId: Long): Set<Long> = queryHandler.getUserRoleIds(userId, tenantId)

    /** @param command 创建角色命令 @param tenantId 租户 ID @return 已创建角色 */
    open fun createRole(command: CreateRoleCommand, tenantId: Long): Role = commandHandler.createRole(
        RoleDraft(command.code, command.name, command.status, false, command.dataScopeType, command.remark, tenantId),
    )

    /** @param id 角色 ID @param command 更新角色命令 @param tenantId 租户 ID @return 更新后角色 */
    open fun updateRole(id: Long, command: UpdateRoleCommand, tenantId: Long): Role {
        val existing = queryHandler.getRole(id, tenantId)
            ?: throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.NOT_FOUND)
        return commandHandler.updateRole(
            existing.copy(name = command.name, status = command.status, dataScopeType = command.dataScopeType, remark = command.remark),
        )
    }

    /** @param id 角色 ID @param tenantId 租户 ID */
    open fun deleteRole(id: Long, tenantId: Long) = commandHandler.deleteRole(id, tenantId)
    /** @param id 角色 ID @param permissionIds 权限 ID 集合 @param tenantId 租户 ID */
    open fun assignRolePermissions(id: Long, permissionIds: Set<Long>, tenantId: Long) =
        commandHandler.assignRolePermissions(id, permissionIds, tenantId)

    /** @param userId 用户 ID @param roleIds 角色 ID 集合 @param tenantId 租户 ID */
    open fun assignUserRoles(userId: Long, roleIds: Set<Long>, tenantId: Long) =
        commandHandler.assignUserRoles(userId, roleIds, tenantId)

    /** @param command 创建菜单命令 @param tenantId 租户 ID @return 已创建菜单 */
    open fun createMenu(command: CreateMenuCommand, tenantId: Long): Menu = commandHandler.createMenu(command.toDraft(tenantId))

    /** @param id 菜单 ID @param command 更新菜单命令 @param tenantId 租户 ID @return 更新后菜单 */
    open fun updateMenu(id: Long, command: UpdateMenuCommand, tenantId: Long): Menu {
        val existing = queryHandler.getMenu(id, tenantId)
            ?: throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.NOT_FOUND)
        return commandHandler.updateMenu(
            existing.copy(
                parentCode = command.parentCode,
                title = command.title,
                type = command.type,
                path = command.path,
                icon = command.icon,
                sort = command.sort,
                visible = command.visible,
                permissionCode = command.permissionCode,
                remark = command.remark,
            ),
        )
    }

    /** @param id 菜单 ID @param tenantId 租户 ID */
    open fun deleteMenu(id: Long, tenantId: Long) = commandHandler.deleteMenu(id, tenantId)
}

/** 创建角色命令。 */
data class CreateRoleCommand(
    val code: String,
    val name: String,
    val status: Short,
    val dataScopeType: DataScopeType,
    val remark: String?,
)

/** 更新角色命令。 */
data class UpdateRoleCommand(
    val name: String,
    val status: Short,
    val dataScopeType: DataScopeType,
    val remark: String?,
)

/** 创建菜单命令。 */
data class CreateMenuCommand(
    val code: String,
    val parentCode: String?,
    val title: String,
    val type: MenuType,
    val path: String?,
    val icon: String?,
    val sort: Int,
    val visible: Boolean,
    val permissionCode: String?,
    val remark: String?,
) {
    fun toDraft(tenantId: Long): MenuDraft = MenuDraft(
        code, parentCode, title, type, path, icon, sort, visible, permissionCode, remark, tenantId,
    )
}

/** 更新菜单命令。 */
data class UpdateMenuCommand(
    val parentCode: String?,
    val title: String,
    val type: MenuType,
    val path: String?,
    val icon: String?,
    val sort: Int,
    val visible: Boolean,
    val permissionCode: String?,
    val remark: String?,
)
