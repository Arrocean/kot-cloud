package com.whitesprite.dev.framework.common.http

/**
 * 全局 API 路由前缀定义
 *
 * 说明：
 * - 仅用于 HTTP API 路由拼接
 * - 不包含任何业务或框架依赖
 *
 * @author WhiteSprite
 */
object ApiPrefix {

    /** 后台管理 API 前缀 */
    const val ADMIN = "/admin-api"

    /** 前台应用 API 前缀 */
    const val APP = "/app-api"
}