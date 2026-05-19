package com.arrocean.dev.framework.security.core.token

import com.arrocean.dev.framework.security.core.context.LoginUser
import io.micronaut.security.authentication.Authentication

/**
 * Token 服务。
 *
 * @author WhiteSprite
 */
interface TokenService {

    /**
     * 生成 Access Token。
     *
     * @param loginUser 登录用户
     * @return Access Token
     */
    fun generateAccessToken(loginUser: LoginUser): String

    /**
     * 将 Micronaut 认证对象转换为系统内的登录用户对象。
     *
     * @param authentication 认证对象
     * @return 登录用户；若缺少必要字段则返回 null
     */
    fun toLoginUser(authentication: Authentication): LoginUser?
}



