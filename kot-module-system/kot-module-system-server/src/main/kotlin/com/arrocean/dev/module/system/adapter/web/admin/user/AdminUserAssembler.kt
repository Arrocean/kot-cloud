package com.arrocean.dev.module.system.adapter.web.admin.user

import com.arrocean.dev.framework.common.util.web.WebUtils
import com.arrocean.dev.module.system.domain.user.model.AdminUser
import com.arrocean.dev.module.system.domain.user.model.AdminUserDraft

/**
 * 领域模型转换器
 *
 * @author WhiteSprite
 */
object AdminUserAssembler {

    /**
     * 创建用户请求转换成领域模型
     *
     * @param createRequest 创建用户请求
     * @return 领域模型
     */
    fun toDomainModel(createRequest: CreateUserRequest): AdminUserDraft {
        return AdminUserDraft(
            username = createRequest.username,
            passwordHash = createRequest.password,
            nickname = createRequest.username,
            remark = null,
            deptId = null,
            email = null,
            mobile = null,
            gender = 0,
            status = 1,
            avatarUrl = null,
            // TODO WhiteSprite：未来从请求上下文中获取当前用户IP，暂时设为127.0.0.1
            loginIp = WebUtils.getClientIP(),
        )
    }

    /**
     * 领域模型转换成响应模型
     *
     * @param domainModel 领域模型
     * @return 响应模型
     */
    fun toGetAdminUserResponse(domainModel: AdminUser): GetAdminUserResponse {
        return GetAdminUserResponse(
            id = domainModel.id,
            username = domainModel.username,
            nickname = domainModel.nickname,
            remark = domainModel.remark ?: "",
            createTime = domainModel.createTime.toEpochMilli(),
            updateTime = domainModel.updateTime.toEpochMilli()
        )
    }
}
