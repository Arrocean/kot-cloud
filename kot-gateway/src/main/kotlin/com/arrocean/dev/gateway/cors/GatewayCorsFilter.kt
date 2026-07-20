package com.arrocean.dev.gateway.cors

import com.arrocean.dev.gateway.config.GatewayProperties
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.filter.ServerFilterPhase
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

/**
 * 在网关边缘统一处理 CORS。
 *
 * 预检请求不进入认证和代理链路，其他响应按允许来源补充 CORS 响应头。
 */
@Filter("/**")
class GatewayCorsFilter(
    private val properties: GatewayProperties,
) : HttpServerFilter {

    /** 返回最早的服务端过滤阶段，确保预检请求先于认证和代理结束。 */
    override fun getOrder(): Int = ServerFilterPhase.FIRST.order()

    /**
     * 处理入站请求的 CORS 协商。
     *
     * 合法预检请求直接返回 204；其他请求继续过滤链，并在响应上补充允许的跨域头。
     *
     * @param request 当前入站请求
     * @param chain 后续服务端过滤链
     * @return 带有适用 CORS 响应头的异步响应发布者
     */
    override fun doFilter(
        request: HttpRequest<*>,
        chain: ServerFilterChain,
    ): Publisher<MutableHttpResponse<*>> {
        val origin = request.headers.origin.orElse(null)
        val allowedOrigin = origin?.takeIf(::isOriginAllowed)
        if (request.method == HttpMethod.OPTIONS && request.headers.contains("Access-Control-Request-Method")) {
            return Mono.just(applyCorsHeaders(HttpResponse.noContent<Any>(), allowedOrigin))
        }
        return Mono.from(chain.proceed(request))
            .map { response -> applyCorsHeaders(response, allowedOrigin) }
    }

    /** 判断来源是否符合显式允许列表或无凭证的通配规则。 */
    private fun isOriginAllowed(origin: String): Boolean {
        val allowedOrigins = properties.cors.allowedOrigins.map(String::trim).filter(String::isNotBlank)
        return origin in allowedOrigins || (!properties.cors.allowCredentials && "*" in allowedOrigins)
    }

    /**
     * 在响应中写入配置的 CORS 头。
     *
     * @param response 需要补充响应头的响应对象
     * @param allowedOrigin 已校验通过的来源；为空时保持响应原样
     * @return 原响应对象，便于响应式链式调用
     */
    private fun applyCorsHeaders(
        response: MutableHttpResponse<*>,
        allowedOrigin: String?,
    ): MutableHttpResponse<*> {
        if (allowedOrigin == null) {
            return response
        }
        response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin)
        response.header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
        response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, properties.cors.allowedMethods.joinToString(","))
        response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, properties.cors.allowedHeaders.joinToString(","))
        response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, properties.cors.exposedHeaders.joinToString(","))
        response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE, properties.cors.maxAgeSeconds.toString())
        if (properties.cors.allowCredentials) {
            response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        }
        return response
    }
}
