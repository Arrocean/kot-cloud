package com.arrocean.dev.gateway.route

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.filter.ServerFilterPhase
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import java.util.UUID

/**
 * 将匹配 Gateway 路由的请求转发至下游服务。
 *
 * 保留 Authorization 以供下游独立验签，清除 hop-by-hop 和客户端伪造的转发头，
 * 并透传下游的状态码、响应头和响应体。
 */
@Filter("/**")
class GatewayProxyFilter(
    private val routeFactory: GatewayRouteFactory,
    private val httpClient: HttpClient,
) : HttpServerFilter {

    /** 返回代理过滤器顺序，使认证完成后才向下游发出请求。 */
    override fun getOrder(): Int = ServerFilterPhase.LAST.order() - 100

    /**
     * 代理命中路由的请求，未命中时交还后续过滤链处理。
     *
     * 下游 HTTP 状态异常会被转换为正常响应透传，连接类异常则继续传播给全局异常处理器。
     *
     * @param request 当前入站请求
     * @param chain 后续过滤链
     * @return 下游响应或未匹配路由的后续响应发布者
     */
    override fun doFilter(
        request: HttpRequest<*>,
        chain: ServerFilterChain,
    ): Publisher<MutableHttpResponse<*>> {
        val resolved = routeFactory.resolver.resolve(request.path) ?: return chain.proceed(request)
        val targetUri = GatewayTargetUriBuilder.build(
            baseUri = resolved.route.targetUri,
            downstreamPath = resolved.downstreamPath,
            rawQuery = request.uri.rawQuery,
        )
        val downstreamRequest = HttpRequest.create<Any>(request.method, targetUri.toString())
        copyRequestHeaders(request, downstreamRequest)
        request.body.orElse(null)?.let { downstreamRequest.body(it) }
        return Mono.from(httpClient.exchange(downstreamRequest, ByteArray::class.java))
            .map(::toGatewayResponse)
            .onErrorResume(HttpClientResponseException::class.java) { exception ->
                Mono.just(toGatewayResponse(exception.response))
            }
    }

    /**
     * 复制可安全转发的请求头，并由网关重建追踪和转发头。
     *
     * @param source 客户端入站请求
     * @param target 将发送至下游的可变请求
     */
    private fun copyRequestHeaders(source: HttpRequest<*>, target: MutableHttpRequest<Any>) {
        source.headers.forEach { name, values ->
            if (
                name.lowercase() !in HOP_BY_HOP_HEADERS &&
                !name.equals(HttpHeaders.HOST, ignoreCase = true) &&
                !name.equals("X-Request-Id", ignoreCase = true) &&
                !name.startsWith("X-Forwarded-", ignoreCase = true)
            ) {
                values.forEach { target.header(name, it) }
            }
        }
        target.header("X-Request-Id", UUID.randomUUID().toString())
        source.remoteAddress.address?.hostAddress?.let { target.header("X-Forwarded-For", it) }
        target.header("X-Forwarded-Proto", source.uri.scheme ?: "http")
        source.headers.get(HttpHeaders.HOST)?.let { target.header("X-Forwarded-Host", it) }
    }

    /**
     * 将下游 HTTP 响应转换为网关响应，保留状态码、端到端响应头和字节响应体。
     *
     * @param response 下游 HTTP Client 返回的响应
     * @return 可直接回写给客户端的网关响应
     */
    private fun toGatewayResponse(response: HttpResponse<*>): MutableHttpResponse<*> {
        val body = response.body.orElse(null) as? ByteArray ?: ByteArray(0)
        val gatewayResponse = HttpResponse.status<ByteArray>(response.status).body(body)
        response.headers.forEach { name, values ->
            if (name.lowercase() !in HOP_BY_HOP_HEADERS) {
                values.forEach { gatewayResponse.header(name, it) }
            }
        }
        return gatewayResponse
    }

    private companion object {
        val HOP_BY_HOP_HEADERS = setOf(
            "connection",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "transfer-encoding",
            "upgrade",
        )
    }
}
