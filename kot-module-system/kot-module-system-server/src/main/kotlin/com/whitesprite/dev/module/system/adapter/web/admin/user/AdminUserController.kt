package com.whitesprite.dev.module.system.adapter.web.admin.user

import com.whitesprite.dev.module.system.api.user.dto.AdminUserDto
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

@Validated
@Controller("/admin/user")
class AdminUserController(
    private val userService: AdminUserAppService
) {
    @Get("/{id}")
    fun getById(@PathVariable id: Long): HttpResponse<GetAdminUserResponse> {
        val domainUser = userService.getById(id) ?: return HttpResponse.notFound()
        val resp = GetAdminUserResponse(
            id = domainUser.id,
            name = domainUser.username,
            nickname = domainUser.nickname,
            remark = domainUser.remark,
            createTime = domainUser.createTime,
            updateTime = domainUser.updateTime
        )
        return HttpResponse.ok(resp)
    }

    @Get
    fun list(
        @QueryValue(defaultValue = "1") page: Int,
        @QueryValue(defaultValue = "10") size: Int,
        @QueryValue(defaultValue = "") keyword: String
    ): HttpResponse<List<AdminUserDto>> {
        val domainUsers = userService.list(keyword)
        val dtos = domainUsers.map {
            AdminUserDto(
                id = it.id,
                username = it.username,
                password = it.password,
                nickname = it.nickname,
                remark = it.remark,
                creator = it.creator,
                createTime = it.createTime,
                updater = it.updater,
                updateTime = it.updateTime,
                deleted = it.deleted
            )
        }
        return HttpResponse.ok(dtos)
    }

    @Post
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid req: CreateUserRequest): Long {
        return userService.create(req)
    }

    @Put("/{id}")
    fun update(
        @PathVariable id: Long,
        @Body @Valid req: UpdateUserRequest
    ): GetAdminUserResponse {
        val domainUser = userService.update(id, req)
        return GetAdminUserResponse(
            id = domainUser.id,
            name = domainUser.username,
            nickname = domainUser.nickname,
            remark = domainUser.remark,
            createTime = domainUser.createTime,
            updateTime = domainUser.updateTime
        )
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}