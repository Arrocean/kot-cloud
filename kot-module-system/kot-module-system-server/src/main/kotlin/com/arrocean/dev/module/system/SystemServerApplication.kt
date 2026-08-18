package com.arrocean.dev.module.system

import io.micronaut.runtime.Micronaut

/**
 * system-server 模块独立启动入口
 *
 * 典型“模块式/服务式”启动写法：
 * - 使用 Micronaut.build() 便于后续扩展（环境、默认配置、banner、lazy-init 等）
 * - 显式声明 mainClass，方便打包与运行时识别入口
 * - 设置默认 application username，便于日志/配置隔离（也可在 application.properties 中配置覆盖）
 */
object SystemServerApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(SystemServerApplication::class.java)
            // 默认应用名（如果 resources/application-system.properties 里配置了 micronaut.application.name，会以配置为准）
//            .properties(mapOf("micronaut.application.name" to "system-server"))
            .start()
    }
}