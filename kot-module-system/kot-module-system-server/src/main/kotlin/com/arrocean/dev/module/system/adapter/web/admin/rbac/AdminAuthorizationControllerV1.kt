package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.adapter.config.CurrentAuthorizationSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.MenuTreeSuccessResponseSchema
import com.arrocean.dev.module.system.application.rbac.core.authorization.MenuNode
import com.arrocean.dev.module.system.application.rbac.core.authorization.RbacAuthorizationHandler
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.micronaut.serde.annotation.Serdeable

/** 当前登录管理员的有效授权响应。 */
@Serdeable
data class CurrentAuthorizationResponse(
    val roleCodes: Set<String>,
    val permissionCodes: Set<String>,
    val superAdmin: Boolean,
)

/**
 * 当前管理员授权信息控制器。
 *
 * @author WhiteSprite
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "授权管理")
@SecurityRequirement(name = "bearerAuth")
@Controller(ApiPrefix.ADMIN_V1 + "/system/authorization")
open class AdminAuthorizationControllerV1(
    private val currentLoginUserProvider: CurrentLoginUserProvider,
    private val authorizationHandler: RbacAuthorizationHandler,
) {
    /** 获取当前管理员的有效角色和功能权限。 */
    @Get("/me")
    @Operation(summary = "获取当前权限", description = "返回当前管理员的有效角色与功能权限")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = [Content(schema = Schema(implementation = CurrentAuthorizationSuccessResponseSchema::class))],
    )
    open fun me(): CommonResult<CurrentAuthorizationResponse> {
        val user = currentLoginUserProvider.requireLoginUser()
        val authorization = authorizationHandler.getAuthorization(user.id, user.tenantId)
        return success(
            CurrentAuthorizationResponse(
                roleCodes = authorization.roleCodes,
                permissionCodes = authorization.permissionCodes,
                superAdmin = authorization.isSuperAdmin,
            ),
        )
    }

    /** 获取当前管理员按功能权限过滤后的菜单树。 */
    @Get("/menus")
    @Operation(summary = "获取当前菜单", description = "返回按当前有效权限过滤后的菜单树")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = [Content(schema = Schema(implementation = MenuTreeSuccessResponseSchema::class))],
    )
    open fun menus(): CommonResult<List<MenuNode>> {
        val user = currentLoginUserProvider.requireLoginUser()
        return success(authorizationHandler.getMenus(user.id, user.tenantId))
    }
}
