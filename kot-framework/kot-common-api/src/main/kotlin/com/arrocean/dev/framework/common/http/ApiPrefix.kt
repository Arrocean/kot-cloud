package com.arrocean.dev.framework.common.http

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

    /** 对外业务 API 前缀 */
    const val API = "/api"

    /* ==================== V1 版本 ==================== */

    /** V1 后台管理 API 前缀 */
    const val ADMIN_V1 = "/v1$ADMIN"

    /** V1 对外业务 API 前缀 */
    const val API_V1 = "/v1$API"

    /* ==================== V2 版本（预留）==================== */

    /** V2 后台管理 API 前缀 */
    const val ADMIN_V2 = "/v2$ADMIN"

    /** V2 前台应用 API 前缀 */
    const val API_V2 = "/v2$API"
}
