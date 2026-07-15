package com.arrocean.dev.module.system.domain.rbac.model

/** 功能权限类型。 */
enum class PermissionType {
    FUNCTION,
}

/** 菜单承载方式。 */
enum class MenuType {
    DIRECTORY,
    INTERNAL,
    EXTERNAL,
    IFRAME,
}

/** 为第二期数据权限预留的数据范围类型。 */
enum class DataScopeType {
    ALL,
    DEPT_AND_CHILDREN,
    DEPT_ONLY,
    SELF,
    CUSTOM_DEPT,
}

/**
 * 角色领域模型。
 *
 * @property id 角色 ID
 * @property code 稳定角色编码
 * @property name 显示名称
 * @property status 启用状态
 * @property builtIn 是否为内置角色
 * @property dataScopeType 数据范围预留配置
 * @property remark 备注
 * @property tenantId 租户 ID
 */
data class Role(
    val id: Long,
    val code: String,
    val name: String,
    val status: Short,
    val builtIn: Boolean,
    val dataScopeType: DataScopeType,
    val remark: String?,
    val tenantId: Long,
)

/** 创建角色时使用的领域草稿。 */
data class RoleDraft(
    val code: String,
    val name: String,
    val status: Short = 0,
    val builtIn: Boolean = false,
    val dataScopeType: DataScopeType = DataScopeType.ALL,
    val remark: String? = null,
    val tenantId: Long = 0,
)

/** 功能权限领域模型。 */
data class Permission(
    val id: Long,
    val code: String,
    val name: String,
    val resource: String,
    val action: String,
    val type: PermissionType,
    val status: Short,
    val remark: String?,
    val tenantId: Long,
)

/** 后端管理的菜单元数据领域模型。 */
data class Menu(
    val id: Long,
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
    val tenantId: Long,
)

/** 创建菜单时使用的领域草稿。 */
data class MenuDraft(
    val code: String,
    val parentCode: String?,
    val title: String,
    val type: MenuType,
    val path: String?,
    val icon: String?,
    val sort: Int = 0,
    val visible: Boolean = true,
    val permissionCode: String? = null,
    val remark: String? = null,
    val tenantId: Long = 0,
)

/**
 * 当前用户最终授权快照。
 *
 * @property roleCodes 有效角色编码集合
 * @property permissionCodes 有效功能权限码集合
 */
data class UserAuthorization(
    val roleCodes: Set<String>,
    val permissionCodes: Set<String>,
) {
    val isSuperAdmin: Boolean get() = SUPER_ADMIN_ROLE_CODE in roleCodes

    companion object {
        const val SUPER_ADMIN_ROLE_CODE = "super_admin"
    }
}
