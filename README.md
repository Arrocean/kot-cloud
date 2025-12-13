# kot-cloud

#### 介绍
该项目旨在尝试构建一个以Kotlin为基本语言的企业级项目后端脚手架。在构建过程中也是本人学习Kotlin的一个过程

#### 软件架构
软件架构说明


#### 安装教程

1.  暂无，等待后续完善项目后进行文档完善

#### 使用说明

1.  暂无，等待后续完善项目后进行文档完善

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)


kot-cloud/                                    # 根项目
│
├── gradle/                                   # Gradle 配置
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml                    # ⭐ 统一版本管理
│
├── build.gradle.kts                          # 根项目构建配置
├── settings.gradle.kts                       # 模块声明
├── gradle.properties                         # 全局属性
├── gradlew / gradlew.bat                     # Gradle wrapper
│
├── kot-framework/                            # 🎯 框架父模块（纯容器）
│   ├── build.gradle.kts                      # 父模块配置（无 application）
│   │
│   ├── kot-framework-common/                 # ⭐ 通用工具模块
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       ├── main/
│   │       │   ├── kotlin/
│   │       │   │   └── com/whitesprite/dev/framework/common/
│   │       │   │       ├── constants/        # 常量
│   │       │   │       │   ├── CommonConstants.kt
│   │       │   │       │   └── ErrorCodes.kt
│   │       │   │       ├── utils/            # 工具类
│   │       │   │       │   ├── StringUtils.kt
│   │       │   │       │   ├── DateUtils.kt
│   │       │   │       │   └── JsonUtils.kt
│   │       │   │       ├── exceptions/       # 通用异常
│   │       │   │       │   ├── BusinessException.kt
│   │       │   │       │   └── SystemException.kt
│   │       │   │       └── response/         # 统一响应
│   │       │   │           ├── Result.kt
│   │       │   │           └── PageResult.kt
│   │       │   └── resources/
│   │       └── test/
│   │           └── kotlin/
│   │
│   ├── kot-framework-exposed/                # ⭐ 数据库模块
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       ├── main/
│   │       │   ├── kotlin/
│   │       │   │   └── com/whitesprite/dev/framework/exposed/
│   │       │   │       ├── config/           # 配置
│   │       │   │       │   ├── DataSourceConfig.kt        # 多数据源配置
│   │       │   │       │   ├── PrimaryDataSource.kt       # 主数据源
│   │       │   │       │   └── SecondaryDataSource.kt     # 从数据源
│   │       │   │       ├── base/             # 基础类
│   │       │   │       │   ├── BaseDO.kt                  # 基础实体（id, createTime, updateTime）
│   │       │   │       │   ├── BaseTable.kt               # 基础表定义
│   │       │   │       │   └── BaseRepository.kt          # 基础仓储
│   │       │   │       ├── query/            # 查询包装器
│   │       │   │       │   ├── KotQueryWrapper.kt         # 类似 LambdaQueryWrapper
│   │       │   │       │   ├── KotUpdateWrapper.kt
│   │       │   │       │   └── QueryExtensions.kt         # Kotlin 扩展函数
│   │       │   │       ├── transaction/      # 事务管理
│   │       │   │       │   ├── TransactionManager.kt
│   │       │   │       │   └── TransactionExtensions.kt
│   │       │   │       └── pagination/       # 分页
│   │       │   │           ├── PageRequest.kt
│   │       │   │           └── PageExtensions.kt
│   │       │   └── resources/
│   │       │       └── application-exposed.conf   # Exposed 相关配置
│   │       └── test/
│   │           └── kotlin/
│   │
│   └── kot-framework-web/                    # ⭐ Web 模块
│       ├── build.gradle.kts
│       └── src/
│           ├── main/
│           │   ├── kotlin/
│           │   │   └── com/whitesprite/dev/framework/web/
│           │   │       ├── config/           # 配置
│           │   │       │   ├── WebConfig.kt
│           │   │       │   ├── XssConfig.kt               # XSS 配置
│           │   │       │   └── ApiPrefixConfig.kt         # API 前缀配置
│           │   │       ├── filter/           # 过滤器
│           │   │       │   ├── XssFilter.kt               # XSS 过滤器
│           │   │       │   ├── XssHttpWrapper.kt          # XSS 包装器
│           │   │       │   └── ApiPrefixPlugin.kt         # API 前缀插件
│           │   │       ├── interceptor/      # 拦截器
│           │   │       │   ├── AuthInterceptor.kt
│           │   │       │   └── LogInterceptor.kt
│           │   │       ├── validation/       # 参数校验
│           │   │       │   ├── ValidatorFactory.kt
│           │   │       │   └── ValidationExtensions.kt
│           │   │       └── routing/          # 路由增强
│           │   │           ├── RoutePrefix.kt             # 路由前缀注解
│           │   │           └── RoutingExtensions.kt       # 路由扩展
│           │   └── resources/
│           │       ├── application-web.conf               # Web 相关配置
│           │       └── xss-whitelist.conf                 # XSS 白名单
│           └── test/
│               └── kotlin/
│
├── service-admin/                            # 🚀 管理端微服务
│   ├── build.gradle.kts                      # ⭐ 这里有 application 配置
│   └── src/
│       ├── main/
│       │   ├── kotlin/
│       │   │   └── com/whitesprite/dev/admin/
│       │   │       ├── Application.kt        # 主入口
│       │   │       ├── routes/               # 路由（自动添加 /admin 前缀）
│       │   │       │   ├── UserRoutes.kt
│       │   │       │   └── SystemRoutes.kt
│       │   │       ├── service/              # 业务逻辑
│       │   │       ├── repository/           # 数据访问
│       │   │       └── model/                # 数据模型
│       │   └── resources/
│       │       └── application.conf          # 服务配置
│       └── test/
│
├── service-user/                             # 🚀 用户端微服务
│   ├── build.gradle.kts                      # ⭐ 这里有 application 配置
│   └── src/
│       ├── main/
│       │   ├── kotlin/
│       │   │   └── com/whitesprite/dev/user/
│       │   │       ├── Application.kt        # 主入口
│       │   │       ├── routes/               # 路由（自动添加 /user 前缀）
│       │   │       │   ├── ProfileRoutes.kt
│       │   │       │   └── OrderRoutes.kt
│       │   │       ├── service/
│       │   │       ├── repository/
│       │   │       └── model/
│       │   └── resources/
│       │       └── application.conf
│       └── test/
│
└── service-gateway/                          # 🚀 API 网关（可选）
├── build.gradle.kts
└── src/