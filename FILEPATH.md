```
kot-cloud/
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml                    # ⭐ 统一版本管理
├── build.gradle.kts                          # 根项目构建配置
├── settings.gradle.kts                       # 模块声明
├── gradlew
├── gradlew.bat                               # Gradle wrapper
├── kot-framework                             # 🎯 框架父模块（纯容器）
│   ├── build.gradle.kts                      # 父模块配置（无 application）
│   ├── kot-common-api/                             # 📦 通用 API 契约层（供各模块依赖：接口/契约/少量公共资源）
│   │   ├── .gitignore
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/
│   │           │       └── whitesprite/
│   │           │           └── dev/
│   │           │               └── package.kt
│   │           └── resources/
│   │               ├── application.properties      # micronaut.application.name=kot-common-api
│   │               └── logback.xml                 # 默认日志输出配置
│   ├── kot-common-model/                           # 🧱 通用模型层（DTO/枚举/异常/分页/校验等基础能力）
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       └── main/
│   │           └── kotlin/
│   │               └── com/
│   │                   └── whitesprite/
│   │                       └── dev/
│   │                           └── framework/
│   │                               └── common/
│   │                                   ├── core/
│   │                                   │   └── CommonList.kt             # KeyValue / ArrayValuable
│   │                                   ├── enums/
│   │                                   │   ├── CommonStatusEnum.kt       # 通用状态枚举 + 扩展函数
│   │                                   │   └── CommonUserTypeEnum.kt     # 通用用户类型枚举 + 扩展函数
│   │                                   ├── exception/
│   │                                   │   ├── ErrorCode.kt
│   │                                   │   ├── ServiceException.kt
│   │                                   │   ├── constants/
│   │                                   │   │   └── GlobalErrorCodeConstants.kt
│   │                                   │   └── util/
│   │                                   │       └── ServiceExceptionUtil.kt
│   │                                   ├── poko/
│   │                                   │   ├── CommonResult.kt           # 通用返回包装 + 便捷函数
│   │                                   │   └── PageModels.kt             # PageParam / PageResult / 排序模型
│   │                                   └── validation/
│   │                                       ├── PageSizeOrNoPage.kt        # 分页参数校验注解
│   │                                       └── PageSizeOrNoPageValidator.kt
│   └── kot-framework-web/                    # ⭐ Web 模块
│       ├── build.gradle.kts
│       └── src/
│           └── main/
│               └── kotlin/
│                   └── com/whitesprite/dev/framework/web/
│                       ├── config/
│                       │   ├── WebConfig.kt
│                       │   ├── XssConfig.kt               # XSS 配置
│                       │   └── ApiPrefixConfig.kt         # API 前缀配置
│                       ├── filter/
│                       │   ├── XssFilter.kt               # XSS 过滤器
│                       │   ├── XssHttpWrapper.kt          # XSS 包装器
│                       │   └── ApiPrefixPlugin.kt         # API 前缀插件
│                       ├── interceptor/
│                       │   ├── AuthInterceptor.kt
│                       │   └── LogInterceptor.kt
│                       ├── validation/
│                       │   ├── ValidatorFactory.kt
│                       │   └── ValidationExtensions.kt
│                       └── routing/
│                           ├── RoutePrefix.kt             # 路由前缀注解
│                           └── RoutingExtensions.kt       # 路由扩展
├── kot-gateway/                                      # 🚀 API 网关（可选）
├── kot-module-system/                                # 🧩 系统模块（按“API 契约”与“Server 实现”拆分）
│   ├── build.gradle.kts                              # 模块聚合构建脚本
│   ├── kot-module-system-api/                        # 📦 对外暴露的 API 契约层（DTO/接口/常量等）
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       └── main/
│   │           └── kotlin/
│   │               └── com/
│   │                   └── whitesprite/
│   │                       └── dev/
│   │                           └── module/
│   │                               └── system/
│   │                                   ├── package.kt
│   │                                   └── api/
│   │                                       └── user/
│   │                                           ├── package.kt
│   │                                           └── dto/
│   │                                               └── UserDTO.kt  # AdminUserDTO
│   └── kot-module-system-server/                     # 🚀 模块服务实现层（Controller/Service/Repository/Entity）
│       ├── build.gradle.kts
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/
│               │       └── whitesprite/
│               │           └── dev/
│               │               └── module/
│               │                   └── system/
│               │                       ├── SystemServerApplication.kt    # 模块独立启动入口
│               │                       ├── controller/
│               │                       │   └── admin/
│               │                       │       ├── auth/
│               │                       │       │   └── package.kt
│               │                       │       └── user/
│               │                       │           ├── AdminUserController.kt
│               │                       │           └── vo/
│               │                       │               └── AdminUserVO.kt
│               │                       ├── service/
│               │                       │   ├── auth/
│               │                       │   │   └── package.kt
│               │                       │   └── user/
│               │                       │       ├── AdminUserService.kt
│               │                       │       └── AdminUserServiceImpl.kt
│               │                       └── persistence/
│               │                           ├── entity/
│               │                           │   ├── tenant/
│               │                           │   │   └── package.kt
│               │                           │   └── user/
│               │                           │       └── AdminUserEntity.kt
│               │                           └── mariadb/
│               │                               ├── tenant/
│               │                               │   └── package.kt
│               │                               └── user/
│               │                                   └── AdminUserRepository.kt
│               └── resources/
│                   ├── application.yml               # 默认配置（端口/数据源占位/日志等）
│                   └── application-local.yml         # 本地环境配置（端口/本地数据源等）
├── kot-server/                               # 🚀 Micronaut 服务（聚合/启动模块）
│   ├── build.gradle.kts                      # kot-server 模块构建脚本（依赖各业务模块）
│   └── src/
│       └── main/
│           ├── kotlin/
│           │   └── com/
│           │       └── whitesprite/
│           │           └── dev/
│           │               └── server/
│           │                   └── KotlinServerApplication.kt
│           │                       # 应用启动入口（Micronaut.build().start()）!!!!!!!!!!!!!!!!单体服务启动入口
│           └── resources/
│               ├── application.yml           # 默认环境配置（端口、数据源占位、dialect 等）
│               └── application-local.yml     # 本地开发配置（如本地端口/本地数据库连接等）
└── build.gradle.kts
```