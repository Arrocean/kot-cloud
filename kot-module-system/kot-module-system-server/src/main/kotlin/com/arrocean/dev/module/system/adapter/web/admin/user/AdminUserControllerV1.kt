package com.arrocean.dev.module.system.adapter.web.admin.user

import com.arrocean.dev.framework.common.http.ApiPrefix
import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.PageResult
import com.arrocean.dev.framework.common.poko.success
import com.arrocean.dev.module.system.adapter.config.AdminUserPageSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.AdminUserSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.CreatedIdListSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.CreatedIdSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.config.StringSuccessResponseSchema
import com.arrocean.dev.module.system.adapter.security.RequirePermission
import com.arrocean.dev.module.system.application.user.core.facade.AdminUserAppService
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
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
 * 用户控制器
 *
 * @author WhiteSprite
 */
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "用户管理")
@SecurityRequirement(name = "bearerAuth")
@Controller(ApiPrefix.ADMIN_V1 + "/system/users")
open class AdminUserControllerV1(
    /**
     * 用户服务
     */
    private val userService: AdminUserAppService
) {

    /**
     * 创建用户
     *
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    @Operation(summary = "创建用户", description = "新增一个后台管理用户")
    @ApiResponse(
        responseCode = "201",
        description = "创建成功，返回用户 ID",
        content = [Content(schema = Schema(implementation = CreatedIdSuccessResponseSchema::class))],
    )
    @Post
    @Status(HttpStatus.CREATED)
    @RequirePermission("system:user:create")
    open suspend fun create(@Body @Valid req: CreateUserRequest): CommonResult<Long?> {
        return success(userService.create(req))
    }

    /**
     * 批量创建用户
     *
     * @param req 创建用户请求列表
     * TODO WhiteSprite：请求体待定...
     */
    @Operation(summary = "批量创建用户", description = "批量新增多个后台管理用户")
    @ApiResponse(
        responseCode = "201",
        description = "批量创建成功，返回用户 ID 列表",
        content = [Content(schema = Schema(implementation = CreatedIdListSuccessResponseSchema::class))],
    )
    @Post("/batch")
    @Status(HttpStatus.CREATED)
    @RequirePermission("system:user:create")
    open fun batchCreate(@Body @Valid req: List<CreateUserRequest>): CommonResult<List<Long>> {
        return success(userService.batchCreate(req))
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    @Operation(summary = "删除用户", description = "根据 ID 删除指定管理员用户")
    @ApiResponse(responseCode = "204", description = "删除成功，无响应体")
    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    @RequirePermission("system:user:delete")
    open suspend fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }

    /**
     * 批量删除
     *
     * @param ids 用户 ID 列表
     */
    @Operation(summary = "批量删除用户", description = "根据 ID 列表批量删除管理员用户")
    @ApiResponse(responseCode = "200", description = "批量删除成功")
    @Delete("/batch/{ids}")
    @RequirePermission("system:user:delete")
    open suspend fun batchDelete(@PathVariable ids: List<Long>) {
        userService.batchDelete(ids)
    }

    /**
     * 更新用户
     *
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    @Operation(summary = "更新用户", description = "根据 ID 更新指定管理员用户的信息")
    @ApiResponse(
        responseCode = "200",
        description = "更新成功，返回用户信息",
        content = [Content(schema = Schema(implementation = AdminUserSuccessResponseSchema::class))],
    )
    @Put("/{id}")
    @RequirePermission("system:user:update")
    open suspend fun update(
        @PathVariable id: Long,
        @Body @Valid req: UpdateUserRequest
    ): CommonResult<GetAdminUserResponse> {
        val domainUser = userService.update(id, req)
        return success(AdminUserAssembler.toGetAdminUserResponse(domainUser))
    }

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    @Operation(summary = "查询用户", description = "根据 ID 获取指定管理员用户的详细信息")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功，返回用户信息",
        content = [Content(schema = Schema(implementation = AdminUserSuccessResponseSchema::class))],
    )
    @Get("/{id}")
    @RequirePermission("system:user:list")
    open suspend fun getById(@PathVariable id: Long): CommonResult<GetAdminUserResponse?> {
        val domainUser = userService.getById(id)
        val resp = domainUser?.let { AdminUserAssembler.toGetAdminUserResponse(it) }
        return success(resp)
    }

    /**
     * 分页查询用户
     *
     * @param query 查询条件
     * @return 用户列表
     */
    @Operation(summary = "分页查询用户", description = "按关键字分页查询管理员用户列表")
    @ApiResponse(
        responseCode = "200",
        description = "查询成功，返回分页数据",
        content = [Content(schema = Schema(implementation = AdminUserPageSuccessResponseSchema::class))],
    )
    @Get
    @RequirePermission("system:user:list")
    open suspend fun page(
        @RequestBean @Valid query: PageAdminUserRequest
    ): CommonResult<PageResult<GetAdminUserResponse>> {
        val page = query.toPageParam()
        val domainUsers = userService.page(page, query.keyword)
        val resp = domainUsers.list.map { AdminUserAssembler.toGetAdminUserResponse(it) }
        return success(PageResult(domainUsers.total, resp))
    }

    /**
     * 测试日志系统
     */
    @Operation(summary = "测试日志", description = "测试日志系统是否正常工作（调试用）")
    @ApiResponse(
        responseCode = "200",
        description = "日志测试触发成功",
        content = [Content(schema = Schema(implementation = StringSuccessResponseSchema::class))],
    )
    @Get("/testLogging")
    open fun testLogging(): CommonResult<String> {
        userService.testLogging()
        return success("日志系统测试成功")
    }
}
