package com.whitesprite.dev.module.system.application.user.core.query

/**
 *  根据用户ID查询用户
 *
 *  @param id 用户ID
 *
 *  @author WhiteSprite
 */
data class GetUserQuery(
    val id: Long
) {
    companion object {
        fun fromId(id: Long): GetUserQuery {
            return GetUserQuery(id = id)
        }
    }
}

/**
 * 根据用户名查询用户
 *
 * @param username 用户名
 */
data class GetUserByUsernameQuery(
    val username: String
) {
    companion object {
        fun fromUsername(username: String) = GetUserByUsernameQuery(username)
    }
}