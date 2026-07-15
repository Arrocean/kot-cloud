package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.module.system.application.rbac.core.facade.CreateMenuCommand
import com.arrocean.dev.module.system.application.rbac.core.facade.CreateRoleCommand
import com.arrocean.dev.module.system.application.rbac.core.facade.UpdateMenuCommand
import com.arrocean.dev.module.system.application.rbac.core.facade.UpdateRoleCommand
import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.Role

object AdminRbacAssembler {
    fun toCommand(request: CreateRoleRequest): CreateRoleCommand = CreateRoleCommand(
        request.code.trim(), request.name.trim(), request.status, request.dataScopeType, request.remark?.trim(),
    )

    fun toCommand(request: UpdateRoleRequest): UpdateRoleCommand = UpdateRoleCommand(
        request.name.trim(), request.status, request.dataScopeType, request.remark?.trim(),
    )

    fun toCommand(request: CreateMenuRequest): CreateMenuCommand = CreateMenuCommand(
        request.code.trim(), request.parentCode?.trim()?.takeIf(String::isNotBlank), request.title.trim(), request.type,
        request.path?.trim()?.takeIf(String::isNotBlank), request.icon?.trim()?.takeIf(String::isNotBlank), request.sort,
        request.visible, request.permissionCode?.trim()?.takeIf(String::isNotBlank), request.remark?.trim(),
    )

    fun toCommand(request: UpdateMenuRequest): UpdateMenuCommand = UpdateMenuCommand(
        request.parentCode?.trim()?.takeIf(String::isNotBlank), request.title.trim(), request.type,
        request.path?.trim()?.takeIf(String::isNotBlank), request.icon?.trim()?.takeIf(String::isNotBlank), request.sort,
        request.visible, request.permissionCode?.trim()?.takeIf(String::isNotBlank), request.remark?.trim(),
    )

    fun toResponse(role: Role): RoleResponse = RoleResponse(
        role.id, role.code, role.name, role.status, role.builtIn, role.dataScopeType, role.remark,
    )

    fun toResponse(permission: Permission): PermissionResponse = PermissionResponse(
        permission.id, permission.code, permission.name, permission.resource, permission.action, permission.type.name,
        permission.status, permission.remark,
    )

    fun toResponse(menu: Menu): MenuResponse = MenuResponse(
        menu.id, menu.code, menu.parentCode, menu.title, menu.type.name, menu.path, menu.icon, menu.sort,
        menu.visible, menu.permissionCode, menu.remark,
    )
}
