package com.arrocean.dev.gateway.route

import java.net.URI

/** Gateway 内部使用的已启用路由定义。 */
data class GatewayRoute(
    val id: String,
    val pathPrefix: String,
    val targetUri: String,
    val stripPrefix: Boolean = false,
)

/** 将入站路径解析到下游路由后的结果。 */
data class ResolvedGatewayRoute(
    val route: GatewayRoute,
    val downstreamPath: String,
)

/**
 * 依据最长路径前缀解析 Gateway 路由。
 *
 * 匹配以路径边界为准，避免 `/api` 错误匹配 `/api-v2`。
 */
class GatewayRouteResolver(routes: List<GatewayRoute>) {

    private val routes = routes
        .filter { it.pathPrefix.isNotBlank() && URI(it.targetUri).isAbsolute }
        .sortedByDescending { normalizedPrefix(it.pathPrefix).length }

    /**
     * 将请求路径解析为最长前缀命中的下游路由。
     *
     * @param path 入站请求路径，不要求预先带有前导斜杠
     * @return 路由及下游路径；无匹配路由时返回 null
     */
    fun resolve(path: String): ResolvedGatewayRoute? {
        val route = routes.firstOrNull { matches(normalizedPrefix(it.pathPrefix), path) } ?: return null
        val prefix = normalizedPrefix(route.pathPrefix)
        val downstreamPath = if (route.stripPrefix) {
            path.removePrefix(prefix).ifBlank { "/" }.ensureLeadingSlash()
        } else {
            path.ensureLeadingSlash()
        }
        return ResolvedGatewayRoute(route, downstreamPath)
    }

    /** 按完整路径段而非简单字符串前缀判断路由是否命中。 */
    private fun matches(prefix: String, path: String): Boolean {
        val normalizedPath = path.ensureLeadingSlash()
        return prefix == "/" || normalizedPath == prefix || normalizedPath.startsWith("$prefix/")
    }

    /** 规范化配置前缀，统一前导斜杠并移除非根路径的尾随斜杠。 */
    private fun normalizedPrefix(value: String): String {
        val normalized = value.trim().ensureLeadingSlash().trimEnd('/')
        return normalized.ifBlank { "/" }
    }

    private fun String.ensureLeadingSlash(): String = if (startsWith('/')) this else "/$this"
}
