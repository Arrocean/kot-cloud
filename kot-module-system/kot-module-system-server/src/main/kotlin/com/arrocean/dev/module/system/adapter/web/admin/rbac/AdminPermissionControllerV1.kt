package com.arrocean.dev.module.system.adapter.web.admin.rbac

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.adapter.security.RequirePermission
import com.arrocean.dev.module.system.application.rbac.core.facade.AdminRbacAppService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation

/**
 * 权限目录控制器。
 *
 * 第一期权限由初始化 SQL 维护，仅提供查询以支持角色授权。
 *
 * @author WhiteSprite
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(ApiPrefix.ADMIN_V1 + "/system/permissions")
open class AdminPermissionControllerV1(
    private val rbacService: AdminRbacAppService,
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) {
    /** 查询权限目录。 */
    @Get
    @RequirePermission("system:role:list")
    @Operation(summary = "查询权限")
    open fun list(): CommonResult<List<PermissionResponse>> = success(
        rbacService.listPermissions(currentLoginUserProvider.requireLoginUser().tenantId).map(AdminRbacAssembler::toResponse),
    )
}
