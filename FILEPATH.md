```
kot-cloud/
├── AGENTS.md                                      # 项目开发约束：DDD、Gradle、Apifox、提交规范
├── README.md                                      # English project introduction
├── README.zh_CN.md                                # 中文项目说明
├── LICENSE
├── build.gradle.kts                               # 根项目构建配置
├── settings.gradle.kts                            # Gradle 模块声明
├── gradle.properties                              # Gradle/KSP 构建参数
├── gradle/
│   ├── libs.versions.toml                         # 集中依赖与插件版本管理
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                                        # Unix Gradle Wrapper
├── gradlew.bat                                    # Windows Gradle Wrapper
├── docker/                                        # 容器化与 Kubernetes 部署配置
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── k8s/
│       ├── base/                                  # 通用 Deployment、Service、Kustomization
│       ├── dev/                                   # 开发环境覆盖配置
│       └── prod/                                  # 生产环境覆盖配置
├── docs/
│   └── adr/
│       └── 0001-rbac-authorization.md             # RBAC 授权与动态菜单架构决策
├── sql/
│   └── kot_cloud_system.sql                       # PostgreSQL 系统表、RBAC 表及初始化数据
├── kot-framework/                                 # 通用框架模块聚合
│   ├── build.gradle.kts
│   ├── kot-common-api/                            # 跨模块公共 API 契约与资源
│   │   └── src/main/
│   │       ├── kotlin/com/arrocean/dev/
│   │       └── resources/application-common.properties
│   ├── kot-common-model/                          # 通用模型、异常、分页、校验与工具类
│   │   └── src/main/kotlin/com/arrocean/dev/framework/common/
│   │       ├── core/                              # BaseEntity、CommonList
│   │       ├── enums/                             # 通用状态与用户类型枚举
│   │       ├── exception/                         # ErrorCode、ServiceException、错误码常量
│   │       ├── poko/                              # CommonResult、PageParam、PageResult
│   │       ├── util/                              # IO、网络、Web 工具
│   │       └── validation/                        # 分页参数校验
│   ├── kot-micronaut-starter-md-core/             # Micronaut Data 通用实体与分页转换
│   │   └── src/main/kotlin/com/arrocean/dev/framework/
│   │       ├── common/core/MDBaseEntity.kt
│   │       ├── common/listener/AuditEntityListener.kt
│   │       └── core/PageConvert.kt
│   ├── kot-micronaut-starter-md-postgresql/       # PostgreSQL Micronaut Data 扩展
│   │   └── src/main/kotlin/com/arrocean/dev/framework/postgresql/
│   │       └── convert/
│   ├── kot-micronaut-starter-security/            # JWT、Redis 会话、密码编码与安全异常处理
│   │   └── src/main/kotlin/com/arrocean/dev/framework/security/
│   │       ├── config/                            # SecurityProperties、Bean/Redis 工厂
│   │       ├── core/                              # 当前登录用户、密码与令牌服务
│   │       └── handler/SecurityExceptionHandlers.kt
│   └── kot-micronaut-starter-web/                 # Serde 与全局 Web 异常处理
│       └── src/main/kotlin/com/arrocean/dev/framework/
│           ├── serde/SerdeImportsConfig.kt
│           └── web/core/handler/GlobalExceptionHandler.kt
├── kot-gateway/                                   # 网关模块占位
│   └── build.gradle.kts
├── kot-module-member/                             # 会员模块占位
│   └── build.gradle.kts
├── kot-module-system/                             # 系统模块：API 契约与 DDD 服务实现
│   ├── build.gradle.kts
│   ├── kot-module-system-api/                     # 对外 API 契约、常量与枚举
│   │   └── src/main/kotlin/com/arrocean/dev/module/system/
│   │       ├── api/
│   │       ├── constants/
│   │       ├── enums/
│   │       └── package.kt
│   └── kot-module-system-server/                  # 系统模块服务实现
│       ├── build.gradle.kts                        # 含 generateApifoxOpenApi 任务
│       └── src/main/
│           ├── kotlin/com/arrocean/dev/module/system/
│           │   ├── SystemServerApplication.kt      # 系统模块独立启动入口
│           │   ├── adapter/                        # 适配层：REST、DTO、Assembler、OpenAPI、安全注解
│           │   │   ├── config/                     # OpenAPI 标签、鉴权与文档响应模型
│           │   │   ├── security/RequirePermission.kt
│           │   │   └── web/
│           │   │       ├── admin/auth/             # 登录、注册、登出接口
│           │   │       ├── admin/rbac/             # 角色、权限、菜单、用户角色、授权接口
│           │   │       ├── admin/user/             # 管理员用户接口
│           │   │       ├── admin/tenant/           # 租户接口预留
│           │   │       └── app/                    # C 端接口预留
│           │   ├── application/                    # 应用层：用例编排、Command/Query/Facade
│           │   │   ├── auth/core/facade/AdminAuthService.kt
│           │   │   ├── log/core/                   # 登录日志应用服务
│           │   │   ├── rbac/core/                  # RBAC 命令、查询、授权处理与 Facade
│           │   │   ├── user/core/                  # 用户 Command、Query 与 Facade
│           │   │   └── tenant/                     # 租户应用层预留
│           │   ├── domain/                         # 领域层：纯模型与 Repository 接口
│           │   │   ├── log/                        # LoginLog 与 LoginLogRepository
│           │   │   ├── rbac/                       # 角色、权限、菜单及关联关系
│           │   │   ├── user/                       # AdminUser 与 AdminUserRepository
│           │   │   └── tenant/                     # 租户领域预留
│           │   ├── infrastructure/                 # 基础设施层：持久化实体与 Repository 实现
│           │   │   └── persistence/
│           │   │       ├── entity/                 # 用户、登录日志、RBAC ORM 实体
│           │   │       ├── mapper/                 # ORM Entity 与领域模型转换
│           │   │       ├── postgresql/             # Micronaut Data PostgreSQL Repository
│           │   │       └── repositoryimpl/         # 领域 Repository PostgreSQL 实现
│           │   └── tool/PasswordHashRunner.kt      # 本地密码哈希辅助入口
│           └── resources/
│               ├── application-system.properties   # 系统模块配置
│               └── openapi.properties              # Micronaut OpenAPI 编译期配置
└── kot-server/                                    # 单体聚合启动模块
    ├── build.gradle.kts                            # 聚合依赖与 Micronaut 应用构建配置
    └── src/main/
        ├── kotlin/com/arrocean/dev/server/
        │   └── KotlinServerApplication.kt          # 单体 Micronaut 启动入口
        └── resources/
            ├── application.properties              # 默认应用配置
            ├── application-local.properties        # 本地开发配置
            └── simplelogger.properties             # 日志配置
```

> 生成的 OpenAPI 文件是构建输出，因此未在上面列出：
> `kot-module-system/kot-module-system-server/build/generated/ksp/main/resources/META-INF/swagger/kot-cloud-api-1.0.0.yml`
> 是完整的运行时路由规范；`kot-cloud-apifox.yml` 是由 `generateApifoxOpenApi` 生成的面向 Apifox 的衍生版本。
