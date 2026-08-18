package com.arrocean.dev.gateway.route

import java.net.URI

/** 构造保留下游基础路径与原始查询参数的代理目标 URI。 */
object GatewayTargetUriBuilder {

    /**
     * 拼接下游服务基础 URI、已解析路径和原始查询参数。
     *
     * @param baseUri 路由配置的下游服务地址，可包含基础路径
     * @param downstreamPath 解析后的下游请求路径
     * @param rawQuery 未解码的原始查询字符串，可为空
     * @return 不包含 fragment 的下游目标 URI
     */
    fun build(baseUri: String, downstreamPath: String, rawQuery: String?): URI {
        val base = URI(baseUri)
        val basePath = base.path?.trimEnd('/').orEmpty()
        val requestPath = downstreamPath.ensureLeadingSlash()
        return URI(
            base.scheme,
            base.authority,
            "$basePath$requestPath",
            rawQuery?.takeIf(String::isNotBlank),
            null,
        )
    }

    private fun String.ensureLeadingSlash(): String = if (startsWith('/')) this else "/$this"
}
