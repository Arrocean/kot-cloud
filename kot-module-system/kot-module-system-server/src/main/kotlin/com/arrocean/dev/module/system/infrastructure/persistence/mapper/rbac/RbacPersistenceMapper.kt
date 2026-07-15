package com.arrocean.dev.module.system.infrastructure.persistence.mapper.rbac

import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.MenuDraft
import com.arrocean.dev.module.system.domain.rbac.model.MenuType
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.PermissionType
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.RoleDraft
import com.arrocean.dev.module.system.domain.rbac.model.DataScopeType
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.MenuEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.PermissionEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.RoleEntity

/**
 * RBAC 领域模型与持久化实体映射器。
 *
 * @author WhiteSprite
 */
object RbacPersistenceMapper {
    fun toDomain(entity: RoleEntity): Role = Role(
        id = requireNotNull(entity.id),
        code = entity.code,
        name = entity.name,
        status = entity.status,
        builtIn = entity.builtIn,
        dataScopeType = DataScopeType.valueOf(entity.dataScopeType),
        remark = entity.remark,
        tenantId = entity.tenantId,
    )

    fun toEntity(draft: RoleDraft): RoleEntity = RoleEntity(
        code = draft.code,
        name = draft.name,
        status = draft.status,
        builtIn = draft.builtIn,
        dataScopeType = draft.dataScopeType.name,
        remark = draft.remark,
        tenantId = draft.tenantId,
    )

    fun toEntity(role: Role): RoleEntity = RoleEntity(
        id = role.id,
        code = role.code,
        name = role.name,
        status = role.status,
        builtIn = role.builtIn,
        dataScopeType = role.dataScopeType.name,
        remark = role.remark,
        tenantId = role.tenantId,
    )

    fun toDomain(entity: PermissionEntity): Permission = Permission(
        id = requireNotNull(entity.id),
        code = entity.code,
        name = entity.name,
        resource = entity.resource,
        action = entity.action,
        type = PermissionType.valueOf(entity.type),
        status = entity.status,
        remark = entity.remark,
        tenantId = entity.tenantId,
    )

    fun toDomain(entity: MenuEntity): Menu = Menu(
        id = requireNotNull(entity.id),
        code = entity.code,
        parentCode = entity.parentCode,
        title = entity.title,
        type = MenuType.valueOf(entity.type),
        path = entity.path,
        icon = entity.icon,
        sort = entity.sort,
        visible = entity.visible,
        permissionCode = entity.permissionCode,
        remark = entity.remark,
        tenantId = entity.tenantId,
    )

    fun toEntity(draft: MenuDraft): MenuEntity = MenuEntity(
        code = draft.code,
        parentCode = draft.parentCode,
        title = draft.title,
        type = draft.type.name,
        path = draft.path,
        icon = draft.icon,
        sort = draft.sort,
        visible = draft.visible,
        permissionCode = draft.permissionCode,
        remark = draft.remark,
        tenantId = draft.tenantId,
    )

    fun toEntity(menu: Menu): MenuEntity = MenuEntity(
        id = menu.id,
        code = menu.code,
        parentCode = menu.parentCode,
        title = menu.title,
        type = menu.type.name,
        path = menu.path,
        icon = menu.icon,
        sort = menu.sort,
        visible = menu.visible,
        permissionCode = menu.permissionCode,
        remark = menu.remark,
        tenantId = menu.tenantId,
    )
}
