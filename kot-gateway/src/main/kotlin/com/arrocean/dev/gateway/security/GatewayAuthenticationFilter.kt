package com.arrocean.dev.gateway.security

import com.arrocean.dev.gateway.config.GatewayProperties
import com.arrocean.dev.gateway.route.GatewayRouteFactory
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.filter.ServerFilterPhase
import io.micronaut.http.filter.HttpServerFilter
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

/**
 * Gateway 入站认证过滤器。
 *
 * 公开路径与 CORS 预检请求直接放行，其他请求必须经 JWT 和 Redis 会话双重校验。
 */
@Filter("/**")
class GatewayAuthenticationFilter(
    properties: GatewayProperties,
    private val authenticationService: GatewayAuthenticationService,
    private val gatewayRouteFactory: GatewayRouteFactory,
) : HttpServerFilter {

    private val publicPathMatcher = PublicPathMatcher(properties.publicPaths)

    /** 返回早于代理过滤器的认证阶段顺序。 */
    override fun getOrder(): Int = ServerFilterPhase.SECURITY.order() - 100

    /**
     * 对非公开且非预检请求执行网关认证。
     *
     * 认证失败直接返回 401；Redis 不可用会继续传播，以便全局异常处理器返回 503。
     *
     * @param request 当前入站请求
     * @param chain 后续服务端过滤链
     * @return 认证后的后续响应、401 响应或基础设施异常发布者
     */
    override fun doFilter(
        request: HttpRequest<*>,
        chain: ServerFilterChain,
    ): Publisher<MutableHttpResponse<*>> {
        if (request.method == HttpMethod.OPTIONS || publicPathMatcher.matches(request.path)) {
            return chain.proceed(request)
        }
        return try {
            val route = gatewayRouteFactory.resolver.resolve(request.path)?.route
                ?: return Mono.just(HttpResponse.notFound<Any>())
            authenticationService.authenticate(request.headers.authorization.orElse(null), route.allowedUserTypes)
            chain.proceed(request)
        } catch (ex: GatewaySessionUnavailableException) {
            return Mono.error(ex)
        } catch (ex: GatewayIdentityVerificationException) {
            return Mono.error(ex)
        } catch (_: GatewayAuthenticationException) {
            Mono.just(HttpResponse.unauthorized<Any>())
        }
    }
}
