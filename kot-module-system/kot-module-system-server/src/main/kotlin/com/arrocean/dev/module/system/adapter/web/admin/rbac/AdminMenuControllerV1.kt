package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.adapter.config.MenuListSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.MenuSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.OperationSuccessResponseSchema
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
 * 菜单配置控制器。
 *
 * 后端维护菜单元数据，前端根据稳定菜单编码解析内部页面组件。
 *
 * @author WhiteSprite
 */
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "菜单管理")
@SecurityRequirement(name = "bearerAuth")
@Controller(ApiPrefix.ADMIN_V1 + "/system/menus")
open class AdminMenuControllerV1(
    private val rbacService: AdminRbacAppService,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    /** 查询当前租户菜单配置。 */
    @Get
    @RequirePermission("system:menu:list")
    @Operation(summary = "查询菜单配置")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = [Content(schema = Schema(implementation = MenuListSuccessResponseSchema::class))],
    )
    open fun list(): CommonResult<List<MenuResponse>> = success(
        rbacService.listMenus(currentLoginUserProvider.requireLoginUser().tenantId).map(AdminRbacAssembler::toResponse),
    )

    /** 创建菜单配置。 */
    @Post
    @Status(HttpStatus.CREATED)
    @RequirePermission("system:menu:create")
    @Operation(summary = "创建菜单配置")
    @ApiResponse(
        responseCode = "201",
        description = "创建成功",
        content = [Content(schema = Schema(implementation = MenuSuccessResponseSchema::class))],
    )
    open fun create(@Body @Valid request: CreateMenuRequest): CommonResult<MenuResponse> {
        val tenantId = currentLoginUserProvider.requireLoginUser().tenantId
        return success(AdminRbacAssembler.toResponse(rbacService.createMenu(AdminRbacAssembler.toCommand(request), tenantId)))
    }

    /** 更新菜单配置。 */
    @Put("/{id}")
    @RequirePermission("system:menu:update")
    @Operation(summary = "更新菜单配置")
    @ApiResponse(
        responseCode = "200",
        description = "更新成功",
        content = [Content(schema = Schema(implementation = MenuSuccessResponseSchema::class))],
    )
    open fun update(@PathVariable id: Long, @Body @Valid request: UpdateMenuRequest): CommonResult<MenuResponse> {
        val tenantId = currentLoginUserProvider.requireLoginUser().tenantId
        return success(AdminRbacAssembler.toResponse(rbacService.updateMenu(id, AdminRbacAssembler.toCommand(request), tenantId)))
    }

    /** 删除菜单配置。 */
    @Delete("/{id}")
    @RequirePermission("system:menu:delete")
    @Operation(summary = "删除菜单配置")
    @ApiResponse(
        responseCode = "200",
        description = "删除成功",
        content = [Content(schema = Schema(implementation = OperationSuccessResponseSchema::class))],
    )
    open fun delete(@PathVariable id: Long): CommonResult<Unit> {
        rbacService.deleteMenu(id, currentLoginUserProvider.requireLoginUser().tenantId)
        return success(Unit)
    }
}
