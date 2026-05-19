package com.arrocean.dev.module.system.adapter.web.admin.auth

import com.arrocean.dev.framework.security.core.context.LoginUser

/**
 * 后台认证接口对象转换器。
 *
 * @author WhiteSprite
 */
object AdminAuthAssembler {

    /**
     * 将当前登录用户转换为后台认证资料响应。
     */
    fun toProfileResponse(loginUser: LoginUser): AdminAuthProfileResponse {
        return AdminAuthProfileResponse(
            id = loginUser.id,
            userType = loginUser.userTypeValue,
            userTypeName = loginUser.userType.name,
            username = loginUser.username,
            nickname = loginUser.nickname,
            deptId = loginUser.deptId,
            email = loginUser.email,
            mobile = loginUser.mobile,
            tenantId = loginUser.tenantId,
            scopes = loginUser.scopes,
            sessionId = loginUser.sessionId,
        )
    }
}


