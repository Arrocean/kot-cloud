package com.whitesprite.dev.module.system.controller.admin.user

import com.whitesprite.dev.module.system.api.user.dto.AdminUserDTO
import com.whitesprite.dev.module.system.controller.admin.user.vo.CreateUserRequestVO
import com.whitesprite.dev.module.system.controller.admin.user.vo.UpdateUserRequestVO
import com.whitesprite.dev.module.system.service.user.AdminUserService
import io.micronaut.http.*
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid

@Validated
@Controller("/admin/user")
class AdminUserController(
    private val userService: AdminUserService
) {
    @Get("/{id}")
    fun getById(@PathVariable id: Long): HttpResponse<AdminUserDTO> {
        val dto = userService.getById(id) ?: return HttpResponse.notFound()
        return HttpResponse.ok(dto)
    }

    @Get
    fun list(
        @QueryValue(defaultValue = "1") page: Int,
        @QueryValue(defaultValue = "10") size: Int,
        @QueryValue(defaultValue = "") keyword: String
    ): HttpResponse<List<AdminUserDTO>> {
        return HttpResponse.ok(userService.list(keyword))
    }

    @Post
    @Status(HttpStatus.CREATED)
    fun create(@Body @Valid req: CreateUserRequestVO): Long {
        return userService.create(req)
    }

    @Put("/{id}")
    fun update(
        @PathVariable id: Long,
        @Body @Valid req: UpdateUserRequestVO
    ): AdminUserDTO {
        return userService.update(id, req)
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}