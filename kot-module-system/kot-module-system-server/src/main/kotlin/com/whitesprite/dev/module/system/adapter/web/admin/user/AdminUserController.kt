package com.whitesprite.dev.module.system.adapter.web.admin.user

import com.whitesprite.dev.module.system.application.user.AdminUserAppService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.Status
import io.micronaut.validation.Validated
import jakarta.validation.Valid

/**
 * 用户控制器
 * @author WhiteSprite
 */
@Validated
@Controller("/admin/user")
class AdminUserController(
    /**
     * 用户服务
     */
    private val userService: AdminUserAppService
) {

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户信息
     */
    @Get("/{id}")
    fun getById(@PathVariable id: Long): HttpResponse<GetAdminUserResponse> {
        val domainUser = userService.getById(id) ?: return HttpResponse.notFound()
        val resp = AdminUserAssembler.toGetAdminUserResponse(domainUser)
        return HttpResponse.ok(resp)
    }

    /**
     * 列表查询用户
     * @param page 页码
     * @param size 页大小
     * @param keyword 关键字
     * @return 用户列表
     */
    @Get
    fun list(
        @QueryValue(defaultValue = "1") page: Int,
        @QueryValue(defaultValue = "10") size: Int,
        @QueryValue(defaultValue = "") keyword: String
    ): HttpResponse<List<GetAdminUserResponse>> {
        val domainUsers = userService.list(keyword)
        val dtos = domainUsers.map { AdminUserAssembler.toGetAdminUserResponse(it) }
        return HttpResponse.ok(dtos)
    }

    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    @Post
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid req: CreateUserRequest): Long {
        return userService.create(req)
    }

    /**
     * 更新用户
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    @Put("/{id}")
    fun update(
        @PathVariable id: Long,
        @Body @Valid req: UpdateUserRequest
    ): GetAdminUserResponse {
        val domainUser = userService.update(id, req)
        return AdminUserAssembler.toGetAdminUserResponse(domainUser)
    }

    /**
     * 删除用户
     * @param id 用户 ID
     */
    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}