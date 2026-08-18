package com.arrocean.dev.server

import io.micronaut.runtime.Micronaut

/**
 * 单体模式 启动入口
 *
 * 典型“模块式/服务式”启动写法：
 * - 使用 Micronaut.build() 便于后续扩展（环境、默认配置、banner、lazy-init 等）
 * - 显式声明 mainClass，方便打包与运行时识别入口
 */
object KotlinServerApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(KotlinServerApplication::class.java)
            .start()
    }
}