package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.adapter.config.OperationSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.UserRoleIdsSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.security.RequirePermission
import com.arrocean.dev.module.system.application.rbac.core.facade.AdminRbacAppService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
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
 * 用户角色分配控制器。
 *
 * @author WhiteSprite
 */
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "用户管理")
@SecurityRequirement(name = "bearerAuth")
@Controller(ApiPrefix.ADMIN_V1 + "/system/users")
open class AdminUserRoleControllerV1(
    private val rbacService: AdminRbacAppService,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    /** 查询指定用户的角色 ID 集合。 */
    @Get("/{userId}/roles")
    @RequirePermission("system:user:list")
    @Operation(summary = "查询用户角色")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = [Content(schema = Schema(implementation = UserRoleIdsSuccessResponseSchema::class))],
    )
    open suspend fun getRoles(@PathVariable userId: Long): CommonResult<Set<Long>> = success(
        rbacService.getUserRoleIds(userId, currentLoginUserProvider.requireLoginUser().tenantId),
    )

    /** 覆盖指定用户的角色集合。 */
    @Put("/{userId}/roles")
    @RequirePermission("system:user:update")
    @Operation(summary = "分配用户角色")
    @ApiResponse(
        responseCode = "200",
        description = "授权成功",
        content = [Content(schema = Schema(implementation = OperationSuccessResponseSchema::class))],
    )
    open suspend fun assignRoles(@PathVariable userId: Long, @Body @Valid request: AssignIdsRequest): CommonResult<Unit> {
        rbacService.assignUserRoles(userId, request.ids, currentLoginUserProvider.requireLoginUser().tenantId)
        return success(Unit)
    }
}
