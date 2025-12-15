# kot-cloud

#### 介绍
该项目旨在尝试构建一个以Kotlin为基本语言的企业级项目后端脚手架。在构建过程中也是本人学习Kotlin的一个过程

#### 软件架构
软件架构说明

本项目致力于构建一个现代化、高性能的微服务架构体系，技术选型如下：

**核心语言**:
- [Kotlin](https://kotlinlang.org/) 2.3.0 - 作为主要开发语言，充分利用其空安全、扩展函数、协程等特性提高开发效率和代码质量

**核心框架**:
- [Micronaut](https://micronaut.io/) - 选择此框架是因为它具有快速启动、低内存占用的特点，特别适合微服务和云原生应用，与Spring Boot相比在性能上有显著优势

**构建工具**:
- [Gradle](https://gradle.org/) - 使用Kotlin DSL作为构建脚本语言，提供更好的IDE支持和类型安全性

**编译优化**:
- [GraalVM](https://www.graalvm.org/) - 用于将应用编译为原生镜像，极大减少启动时间和内存消耗，充分发挥Kotlin的优势


#### 安装教程

1.  暂无，等待后续完善项目后进行文档完善

#### 使用说明

1.  暂无，等待后续完善项目后进行文档完善

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

```
kot-cloud/
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml                    # ⭐ 统一版本管理
├── build.gradle.kts                          # 根项目构建配置
├── settings.gradle.kts                       # 模块声明
├── gradle.properties                         # 全局属性
├── gradlew
├── gradlew.bat                               # Gradle wrapper
├── kot-framework                             # 🎯 框架父模块（纯容器）
│   ├── build.gradle.kts                      # 父模块配置（无 application）
│   ├── kot-common/                           # ⭐ 通用工具模块
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       └── main/
│   │           └── kotlin/
│   │               └── com/whitesprite/dev/framework/common/
│   │                   ├── constants/
│   │                   │   ├── CommonConstants.kt
│   │                   │   └── ErrorCodes.kt
│   │                   ├── utils/
│   │                   │   ├── StringUtils.kt
│   │                   │   ├── DateUtils.kt
│   │                   │   └── JsonUtils.kt
│   │                   ├── exceptions/
│   │                   │   ├── BusinessException.kt
│   │                   │   └── SystemException.kt
│   │                   └── response/
│   │                       ├── Result.kt
│   │                       └── PageResult.kt
│   ├── kot-framework-exposed/                # ⭐ 数据库模块
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       └── main/
│   │           └── kotlin/
│   │               └── com/whitesprite/dev/framework/exposed/
│   │                   ├── config/
│   │                   │   ├── DataSourceConfig.kt        # 多数据源配置
│   │                   │   ├── PrimaryDataSource.kt       # 主数据源
│   │                   │   └── SecondaryDataSource.kt     # 从数据源
│   │                   ├── base/
│   │                   │   ├── BaseDO.kt                  # 基础实体（id, createTime, updateTime）
│   │                   │   ├── BaseTable.kt               # 基础表定义
│   │                   │   └── BaseRepository.kt          # 基础仓储
│   │                   ├── query/
│   │                   │   ├── KotQueryWrapper.kt         # 类似 LambdaQueryWrapper
│   │                   │   ├── KotUpdateWrapper.kt
│   │                   │   └── QueryExtensions.kt         # Kotlin 扩展函数
│   │                   ├── transaction/
│   │                   │   ├── TransactionManager.kt
│   │                   │   └── TransactionExtensions.kt
│   │                   └── pagination/
│   │                       ├── PageRequest.kt
│   │                       └── PageExtensions.kt
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
├── kot-system/                            # 🚀 管理端微服务
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           └── kotlin/
│               └── com/whitesprite/dev/admin/
│                   ├── Application.kt        # 主入口
│                   ├── routes/               # 路由（自动添加 /admin 前缀）
│                   │   ├── UserRoutes.kt
│                   │   └── SystemRoutes.kt
│                   ├── service/              # 业务逻辑
│                   ├── repository/           # 数据访问
│                   └── model/                # 数据模型
├── service-gateway/                          # 🚀 API 网关（可选）
└── build.gradle.kts
```