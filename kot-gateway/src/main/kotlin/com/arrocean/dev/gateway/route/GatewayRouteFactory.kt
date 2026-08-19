package com.arrocean.dev.gateway.route

import com.arrocean.dev.gateway.config.GatewayProperties
import jakarta.inject.Singleton

/**
 * 将配置绑定的路由转换为不可变的路由解析器。
 *
 * 禁用路由不会进入代理匹配范围。
 */
@Singleton
class GatewayRouteFactory(properties: GatewayProperties) {

    /** 已按 enabled 标记过滤的不可变路由解析器。 */
    val resolver = GatewayRouteResolver(
        properties.routes
            .filter { it.enabled }
            .map {
                GatewayRoute(
                    id = it.id,
                    pathPrefix = it.pathPrefix,
                    targetUri = it.targetUri,
                    stripPrefix = it.stripPrefix,
                    allowedUserTypes = it.allowedUserTypes.map(String::trim).filter(String::isNotBlank).toSet(),
                )
            }
    )
}
