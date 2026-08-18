package com.arrocean.dev.module.system.adapter.config

import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminAuthProfileResponse
import com.arrocean.dev.module.system.adapter.web.admin.auth.AdminLoginResponse
import com.arrocean.dev.module.system.adapter.web.admin.rbac.CurrentAuthorizationResponse
import com.arrocean.dev.module.system.adapter.web.admin.rbac.MenuResponse
import com.arrocean.dev.module.system.adapter.web.admin.rbac.PermissionResponse
import com.arrocean.dev.module.system.adapter.web.admin.rbac.RoleResponse
import com.arrocean.dev.module.system.adapter.web.admin.user.GetAdminUserResponse
import com.arrocean.dev.module.system.application.rbac.core.authorization.MenuNode
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Apifox兼容的成功响应模式。
 *
 * 这些类型仅覆盖OpenAPI响应文档。运行时终结点继续返回
 * `CommonResult<T>`，因此保留共享JSON响应契约。
 *
 * @author WhiteSprite
 */
@Schema(name = "AdminLoginSuccessResponse", description = "管理员登录或注册成功响应")
data class AdminLoginSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: AdminLoginResponse,
)

@Schema(name = "AdminProfileSuccessResponse", description = "当前管理员信息成功响应")
data class AdminProfileSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: AdminAuthProfileResponse,
)

@Schema(name = "CurrentAuthorizationSuccessResponse", description = "当前有效授权成功响应")
data class CurrentAuthorizationSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: CurrentAuthorizationResponse,
)

@Schema(name = "MenuTreeSuccessResponse", description = "当前可见菜单树成功响应")
data class MenuTreeSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: List<MenuNode>,
)

@Schema(name = "RoleListSuccessResponse", description = "角色列表成功响应")
data class RoleListSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: List<RoleResponse>,
)

@Schema(name = "RoleSuccessResponse", description = "角色详情成功响应")
data class RoleSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: RoleResponse,
)

@Schema(name = "PermissionListSuccessResponse", description = "权限列表成功响应")
data class PermissionListSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: List<PermissionResponse>,
)

@Schema(name = "MenuListSuccessResponse", description = "菜单配置列表成功响应")
data class MenuListSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: List<MenuResponse>,
)

@Schema(name = "MenuSuccessResponse", description = "菜单配置成功响应")
data class MenuSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: MenuResponse,
)

@Schema(name = "UserRoleIdsSuccessResponse", description = "用户角色 ID 集合成功响应")
data class UserRoleIdsSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: Set<Long>,
)

@Schema(name = "OperationSuccessResponse", description = "无业务数据操作成功响应")
data class OperationSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: Map<String, Any> = emptyMap(),
)

@Schema(name = "AdminUserSuccessResponse", description = "管理员用户成功响应")
data class AdminUserSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: GetAdminUserResponse,
)

@Schema(name = "AdminUserPageData", description = "管理员用户分页数据")
data class AdminUserPageDataSchema(
    val total: Long,
    val list: List<GetAdminUserResponse>,
)

@Schema(name = "AdminUserPageSuccessResponse", description = "管理员用户分页成功响应")
data class AdminUserPageSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: AdminUserPageDataSchema,
)

@Schema(name = "CreatedIdSuccessResponse", description = "创建资源 ID 成功响应")
data class CreatedIdSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: Long,
)

@Schema(name = "CreatedIdListSuccessResponse", description = "批量创建资源 ID 成功响应")
data class CreatedIdListSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: List<Long>,
)

@Schema(name = "BooleanSuccessResponse", description = "布尔结果成功响应")
data class BooleanSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: Boolean,
)

@Schema(name = "StringSuccessResponse", description = "字符串结果成功响应")
data class StringSuccessResponseSchema(
    val code: Int,
    val msg: String,
    val data: String,
)
