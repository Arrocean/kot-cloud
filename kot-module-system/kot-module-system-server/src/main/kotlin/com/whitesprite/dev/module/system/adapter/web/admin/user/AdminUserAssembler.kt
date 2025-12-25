package com.whitesprite.dev.module.system.adapter.web.admin.user

import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import java.time.LocalDateTime

/**
 * 领域模型转换器
 *
 * @author WhiteSprite
 */
object AdminUserAssembler {
    /**
     * 创建用户请求转换成领域模型
     *
     * @param domainModel 领域模型
     * @return 创建用户请求
     */
    fun toCreateUserRequest(domainModel: AdminUser): CreateUserRequest {
        return CreateUserRequest(
            name = domainModel.username,
            password = domainModel.password
        )
    }

    /**
     * 更新用户请求转换成领域模型
     *
     * @param domainModel 领域模型
     * @return 更新用户请求
     */
    fun toUpdateUserRequest(domainModel: AdminUser): UpdateUserRequest {
        return UpdateUserRequest(
            name = domainModel.username
        )
    }

    /**
     * 创建用户请求转换成领域模型
     *
     * @param createRequest 创建用户请求
     * @return 领域模型
     */
    fun toDomainModel(createRequest: CreateUserRequest): AdminUser {
        return AdminUser(
            id = 0L, // 新创建的用户ID通常为0或null
            username = createRequest.name,
            password = createRequest.password,
            nickname = createRequest.name,
            remark = "testUser",
            creator = "TODO()",
            createTime = LocalDateTime.now(),
            updater = "TODO()",
            updateTime = LocalDateTime.now(),
            deleted = false
        )
    }

    /**
     * 获取用户信息转换成响应模型
     *
     * @param domainModel 领域模型
     * @return 响应模型
     */
    fun toGetAdminUserResponse(domainModel: AdminUser): GetAdminUserResponse {
        return GetAdminUserResponse(
            id = domainModel.id,
            name = domainModel.username,
            nickname = domainModel.nickname,
            remark = domainModel.remark,
            createTime = domainModel.createTime,
            updateTime = domainModel.updateTime
        )
    }
}