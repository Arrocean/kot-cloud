package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.module.system.domain.rbac.model.DataScopeType
import com.arrocean.dev.module.system.domain.rbac.model.MenuType
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class CreateRoleRequest(
    @field:NotBlank @field:Size(max = 128) val code: String,
    @field:NotBlank @field:Size(max = 128) val name: String,
    val status: Short = 0,
    val dataScopeType: DataScopeType = DataScopeType.ALL,
    @field:Size(max = 256) val remark: String? = null,
)

@Serdeable
data class UpdateRoleRequest(
    @field:NotBlank @field:Size(max = 128) val name: String,
    val status: Short = 0,
    val dataScopeType: DataScopeType = DataScopeType.ALL,
    @field:Size(max = 256) val remark: String? = null,
)

@Serdeable
data class AssignIdsRequest(
    val ids: Set<Long>,
)

@Serdeable
data class RoleResponse(
    val id: Long,
    val code: String,
    val name: String,
    val status: Short,
    val builtIn: Boolean,
    val dataScopeType: DataScopeType,
    val remark: String?,
)

@Serdeable
data class PermissionResponse(
    val id: Long,
    val code: String,
    val name: String,
    val resource: String,
    val action: String,
    val type: String,
    val status: Short,
    val remark: String?,
)

@Serdeable
data class CreateMenuRequest(
    @field:NotBlank @field:Size(max = 128) val code: String,
    @field:Size(max = 128) val parentCode: String? = null,
    @field:NotBlank @field:Size(max = 128) val title: String,
    val type: MenuType,
    @field:Size(max = 1024) val path: String? = null,
    @field:Size(max = 128) val icon: String? = null,
    val sort: Int = 0,
    val visible: Boolean = true,
    @field:Size(max = 128) val permissionCode: String? = null,
    @field:Size(max = 256) val remark: String? = null,
)

@Serdeable
data class UpdateMenuRequest(
    @field:Size(max = 128) val parentCode: String? = null,
    @field:NotBlank @field:Size(max = 128) val title: String,
    val type: MenuType,
    @field:Size(max = 1024) val path: String? = null,
    @field:Size(max = 128) val icon: String? = null,
    val sort: Int = 0,
    val visible: Boolean = true,
    @field:Size(max = 128) val permissionCode: String? = null,
    @field:Size(max = 256) val remark: String? = null,
)

@Serdeable
data class MenuResponse(
    val id: Long,
    val code: String,
    val parentCode: String?,
    val title: String,
    val type: String,
    val path: String?,
    val icon: String?,
    val sort: Int,
    val visible: Boolean,
    val permissionCode: String?,
    val remark: String?,
)
