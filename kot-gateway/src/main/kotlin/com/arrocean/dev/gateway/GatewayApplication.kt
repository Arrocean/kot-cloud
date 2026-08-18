package com.arrocean.dev.gateway

import io.micronaut.runtime.Micronaut

/**
 * Gateway 独立服务启动入口。
 *
 * 该模块不依赖业务模块或安全 Starter，可作为单独微服务启动。
 */
object GatewayApplication {

    /**
     * 启动独立的 Micronaut Gateway 上下文和 HTTP 服务。
     *
     * @param args 传递给 Micronaut 的启动参数
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(GatewayApplication::class.java)
            .start()
    }
}
