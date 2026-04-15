package com.whitesprite.dev.module.system.adapter.web.admin.user

import com.whitesprite.dev.framework.common.http.ApiPrefix
import com.whitesprite.dev.framework.common.poko.CommonResult
import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.framework.common.poko.success
import com.whitesprite.dev.module.system.application.user.core.facade.AdminUserAppService
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

/**
 * 用户控制器
 *
 * @author WhiteSprite
 */
@Validated
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
    @Post
    @Status(HttpStatus.CREATED)
    open fun create(@Body @Valid req: CreateUserRequest): CommonResult<Long?> {
        return success(userService.create(req))
    }

    /**
     * 批量创建用户
     *
     * @param req 创建用户请求列表
     * TODO WhiteSprite：请求体待定...
     */
    @Post("/batch")
    @Status(HttpStatus.CREATED)
    open fun batchCreate(@Body @Valid req: List<CreateUserRequest>): CommonResult<List<Long?>> {
        return success(userService.batchCreate(req))
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    open fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }

    /**
     * 批量删除
     *
     * @param ids 用户 ID 列表
     */
    @Delete("/batch/{ids}")
    open fun batchDelete(@PathVariable ids: List<Long>) {
        userService.batchDelete(ids)
    }

    /**
     * 更新用户
     *
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    @Put("/{id}")
    open fun update(
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
    @Get("/{id}")
    open fun getById(@PathVariable id: Long): CommonResult<GetAdminUserResponse?> {
        val domainUser = userService.getById(id)
        val resp = domainUser?.let { AdminUserAssembler.toGetAdminUserResponse(it) }
        return success(resp)
    }

    /**
     * 分页查询用户
     *
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param keyword 关键字
     * @return 用户列表
     */
    @Get
    open fun page(
        @QueryValue(defaultValue = "1") pageNo: Int,
        @QueryValue(defaultValue = "10") pageSize: Int,
        @QueryValue(defaultValue = "") keyword: String
    ): CommonResult<PageResult<GetAdminUserResponse>> {
        val domainUsers = userService.page(pageNo, pageSize, keyword)
        val resp = domainUsers.list.map { AdminUserAssembler.toGetAdminUserResponse(it) }
        return success(PageResult(domainUsers.total, resp))
    }

    /**
     * 测试日志系统
     */
    @Get("/testLogging")
    open fun testLogging(): CommonResult<String> {
        userService.testLogging()
        return success("日志系统测试成功")
    }
}