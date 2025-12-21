package com.whitesprite.dev.server

import com.whitesprite.dev.module.system.SystemServerApplication
import io.micronaut.runtime.Micronaut

object KotlinServerApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .mainClass(KotlinServerApplication::class.java)
            // 默认应用名（如果 resources/application.properties 里配置了 micronaut.application.name，会以配置为准）
            .properties(mapOf("micronaut.application.name" to "kot-server"))
            .start()
    }
}