package com.arrocean.dev.module.system.adapter.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Singleton

@OpenAPIDefinition(
    info = Info(
        title = "kot-cloud API",
        version = "1.0.0",
        description = "快速开发平台 - 系统模块接口文档",
    ),
    servers = [Server(url = "http://localhost:1164/v1/admin-api")],
    tags = [
        Tag(name = "认证管理", description = "管理员登录、登出、注册和个人资料接口"),
        Tag(name = "用户管理", description = "后台管理员用户及其角色分配接口"),
        Tag(name = "授权管理", description = "当前登录用户的授权信息和菜单接口"),
        Tag(name = "角色管理", description = "角色及角色权限配置接口"),
        Tag(name = "权限管理", description = "功能权限目录查询接口"),
        Tag(name = "菜单管理", description = "后台动态菜单配置接口"),
    ],
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
)
@Singleton
class OpenApiDefinition {
}
