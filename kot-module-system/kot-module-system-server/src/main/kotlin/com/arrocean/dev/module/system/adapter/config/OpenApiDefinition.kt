package com.arrocean.dev.module.system.adapter.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import jakarta.inject.Singleton

@OpenAPIDefinition(
    info = Info(
        title = "kot-cloud API",
        version = "1.0.0",
        description = "快速开发平台 - 系统模块接口文档",
    ),
    servers = [Server(url = "http://localhost:8002")]
)
@Singleton
class OpenApiDefinition {
}