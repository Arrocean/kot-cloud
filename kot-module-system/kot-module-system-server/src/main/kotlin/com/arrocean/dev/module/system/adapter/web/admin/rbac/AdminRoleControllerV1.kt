package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.adapter.config.OperationSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.RoleListSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.RoleSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.security.RequirePermission
import com.arrocean.dev.module.system.application.rbac.core.facade.AdminRbacAppService
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

/**
 * 角色管理控制器。
 *
 * `super_admin` 不在该控制器的任何可管理路径中暴露。
 *
 * @author WhiteSprite
 */
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "角色管理")
@SecurityRequirement(name = "bearerAuth")
@Controller(ApiPrefix.ADMIN_V1 + "/system/roles")
open class AdminRoleControllerV1(
    private val rbacService: AdminRbacAppService,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    /** 查询可管理角色列表。 */
    @Get
    @RequirePermission("system:role:list")
    @Operation(summary = "查询角色")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = [Content(schema = Schema(implementation = RoleListSuccessResponseSchema::class))],
    )
    open fun list(): CommonResult<List<RoleResponse>> = success(
        rbacService.listRoles(currentLoginUserProvider.requireLoginUser().tenantId).map(AdminRbacAssembler::toResponse),
    )

    /** 创建普通角色。 */
    @Post
    @Status(HttpStatus.CREATED)
    @RequirePermission("system:role:create")
    @Operation(summary = "创建角色")
    @ApiResponse(
        responseCode = "201",
        description = "创建成功",
        content = [Content(schema = Schema(implementation = RoleSuccessResponseSchema::class))],
    )
    open fun create(@Body @Valid request: CreateRoleRequest): CommonResult<RoleResponse> {
        val tenantId = currentLoginUserProvider.requireLoginUser().tenantId
        return success(AdminRbacAssembler.toResponse(rbacService.createRole(AdminRbacAssembler.toCommand(request), tenantId)))
    }

    /** 更新角色基础配置。 */
    @Put("/{id}")
    @RequirePermission("system:role:update")
    @Operation(summary = "更新角色")
    @ApiResponse(
        responseCode = "200",
        description = "更新成功",
        content = [Content(schema = Schema(implementation = RoleSuccessResponseSchema::class))],
    )
    open fun update(@PathVariable id: Long, @Body @Valid request: UpdateRoleRequest): CommonResult<RoleResponse> {
        val tenantId = currentLoginUserProvider.requireLoginUser().tenantId
        return success(AdminRbacAssembler.toResponse(rbacService.updateRole(id, AdminRbacAssembler.toCommand(request), tenantId)))
    }

    /** 覆盖角色拥有的功能权限。 */
    @Put("/{id}/permissions")
    @RequirePermission("system:role:update")
    @Operation(summary = "分配角色权限")
    @ApiResponse(
        responseCode = "200",
        description = "授权成功",
        content = [Content(schema = Schema(implementation = OperationSuccessResponseSchema::class))],
    )
    open fun assignPermissions(@PathVariable id: Long, @Body @Valid request: AssignIdsRequest): CommonResult<Unit> {
        rbacService.assignRolePermissions(id, request.ids, currentLoginUserProvider.requireLoginUser().tenantId)
        return success(Unit)
    }

    /** 删除普通非内置角色。 */
    @Delete("/{id}")
    @RequirePermission("system:role:delete")
    @Operation(summary = "删除角色")
    @ApiResponse(
        responseCode = "200",
        description = "删除成功",
        content = [Content(schema = Schema(implementation = OperationSuccessResponseSchema::class))],
    )
    open fun delete(@PathVariable id: Long): CommonResult<Unit> {
        rbacService.deleteRole(id, currentLoginUserProvider.requireLoginUser().tenantId)
        return success(Unit)
    }
}
