package com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

/**
 * 角色 ORM 实体，对应 `system_role`。
 *
 * @author WhiteSprite
 */
@MappedEntity("system_role")
data class RoleEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var code: String,
    var name: String,
    var status: Short = 0,
    var builtIn: Boolean = false,
    var dataScopeType: String = "ALL",
    var remark: String? = null,
    var tenantId: Long = 0,
)

/**
 * 权限 ORM 实体，对应 `system_permission`。
 *
 * @author WhiteSprite
 */
@MappedEntity("system_permission")
data class PermissionEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var code: String,
    var name: String,
    var resource: String,
    var action: String,
    var type: String = "FUNCTION",
    var status: Short = 0,
    var remark: String? = null,
    var tenantId: Long = 0,
)

/**
 * 菜单 ORM 实体，对应 `system_menu`。
 *
 * @author WhiteSprite
 */
@MappedEntity("system_menu")
data class MenuEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var code: String,
    var parentCode: String? = null,
    var title: String,
    var type: String,
    var path: String? = null,
    var icon: String? = null,
    var sort: Int = 0,
    var visible: Boolean = true,
    var permissionCode: String? = null,
    var remark: String? = null,
    var tenantId: Long = 0,
)

/**
 * 用户角色关联 ORM 实体，对应 `system_user_role`。
 *
 * @author WhiteSprite
 */
@MappedEntity("system_user_role")
data class UserRoleEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var userId: Long,
    var roleId: Long,
    var tenantId: Long = 0,
)

/**
 * 角色权限关联 ORM 实体，对应 `system_role_permission`。
 *
 * @author WhiteSprite
 */
@MappedEntity("system_role_permission")
data class RolePermissionEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var roleId: Long,
    var permissionId: Long,
    var tenantId: Long = 0,
)
