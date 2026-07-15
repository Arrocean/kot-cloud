package com.arrocean.dev.framework.common.util.web

import com.arrocean.dev.framework.common.util.net.NetUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.context.ServerRequestContext
import java.net.InetAddress
import java.nio.charset.StandardCharsets

/**
 * Web 请求工具类。
 *
 * 说明：
 * - 原始 Java 版本基于 Spring MVC + Servlet API
 * - 当前项目使用 Micronaut，因此这里改为基于 `HttpRequest` / `HttpResponse`
 * - `writeJson` 在 Micronaut 中更推荐直接返回对象或 `HttpResponse`，这里保留一个兼容风格的工具方法
 */
object WebUtils {

	private val DEFAULT_IP_HEADER_NAMES = arrayOf(
		"X-Forwarded-For",
		"X-Real-IP",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_CLIENT_IP",
		"HTTP_X_FORWARDED_FOR",
		"Forwarded",
	)

	/**
	 * 返回 JSON 响应。
	 */
	@JvmStatic
	fun <T : Any> writeJson(response: MutableHttpResponse<T>, body: T): MutableHttpResponse<T> {
		response.contentType(MediaType.APPLICATION_JSON_TYPE)
		return response.body(body)
	}

	/**
	 * 快速创建一个 JSON 响应。
	 */
	@JvmStatic
	fun <T : Any> json(body: T): MutableHttpResponse<T> {
		return HttpResponse.ok(body).contentType(MediaType.APPLICATION_JSON_TYPE)
	}

	/**
	 * @param request 请求
	 * @return ua
	 */
	@JvmStatic
	fun getUserAgent(request: HttpRequest<*>): String {
		return request.headers.get(HttpHeaders.USER_AGENT).orEmpty()
	}

	/**
	 * 获得当前请求。
	 */
	@JvmStatic
	fun getRequest(): HttpRequest<*>? {
		return ServerRequestContext.currentRequest<Any>().orElse(null)
	}

	@JvmStatic
	fun getUserAgent(): String? {
		val request = getRequest() ?: return null
		return getUserAgent(request)
	}

	@JvmStatic
	fun getClientIP(): InetAddress? {
		val request = getRequest() ?: return null
		return getClientIP(request)
	}

	@JvmStatic
	fun getClientIP(request: HttpRequest<*>, vararg otherHeaderNames: String): InetAddress? {
		val headerNames = if (otherHeaderNames.isEmpty()) {
			DEFAULT_IP_HEADER_NAMES
		} else {
			arrayOf(*otherHeaderNames, *DEFAULT_IP_HEADER_NAMES)
		}
		return getClientIPByHeader(request, *headerNames)
	}

	@JvmStatic
	fun isJsonRequest(request: HttpRequest<*>): Boolean {
		val contentType = request.contentType.orElse(null)?.toString() ?: return false
		return contentType.startsWith(MediaType.APPLICATION_JSON, ignoreCase = true) ||
			contentType.contains("+json", ignoreCase = true)
	}

	@JvmStatic
	fun getBody(request: HttpRequest<*>): String? {
		if (!isJsonRequest(request)) {
			return null
		}
		return request.getBody(String::class.java).orElse(null)
	}

	@JvmStatic
	fun getBodyBytes(request: HttpRequest<*>): ByteArray? {
		if (!isJsonRequest(request)) {
			return null
		}
		return request.getBody(ByteArray::class.java).orElse(null)
			?: getBody(request)?.toByteArray(StandardCharsets.UTF_8)
	}

	@JvmStatic
	fun getClientIPByHeader(request: HttpRequest<*>, vararg headerNames: String): InetAddress? {
		for (headerName in headerNames) {
			val headerValue = request.headers.get(headerName)
			val ip = if (headerName.equals("Forwarded", ignoreCase = true)) {
				NetUtils.getIpFromForwardedHeader(headerValue)
			} else {
				NetUtils.getMultistageReverseProxyIp(headerValue)
			}
			if (ip != null) {
				return ip
			}
		}

		request.remoteAddress.address?.let { return it }

		val remoteIp = request.remoteAddress.hostString?.takeIf(String::isNotBlank)
		return NetUtils.getMultistageReverseProxyIp(remoteIp)
	}

	@JvmStatic
	fun getParamMap(request: HttpRequest<*>): Map<String, String> {
		return request.parameters.names().associateWith { name ->
			request.parameters.get(name).orEmpty()
		}
	}

	@JvmStatic
	fun getHeaderMap(request: HttpRequest<*>): Map<String, String> {
		return request.headers.names().associateWith { name ->
			request.headers.get(name).orEmpty()
		}
	}
}


