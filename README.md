# kot-cloud

#### 介绍
该项目旨在尝试构建一个以Kotlin为基本语言的企业级项目后端脚手架。在构建过程中也是本人学习Kotlin的一个过程

#### 说明
该项目旨在构建一个基于 Kotlin 的企业级微服务脚手架，目标是在功能上对标 SpringCloudAlibaba + SpringBoot + Maven + MybatisPlus + JVM 生态，同时利用 Kotlin 与 GraalVM 的特性，尽可能提升 CPU 效率并优化内存占用。
项目定位为企业级模板与参考实现，包含常用的模块划分（公共工具、数据库、Web 层、管理端服务、可选网关），便于在生产级项目中直接复用或作为二次开发基础。

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

**ORM**: 混合使用

- [Micronaut Data](https://micronaut-projects.github.io/micronaut-data/latest/guide/) - 用于数据访问层，提供类型安全的ORM能力

- [Exposed](https://github.com/JetBrains/Exposed) - 用于关系型数据库访问，提供类型安全的 SQL 构建器

**数据库**:

- [MariaDB](https://mariadb.org/) - 用于关系型数据库


#### 安装教程

1.  暂无，等待后续完善项目后进行文档完善

#### 使用说明

1.  暂无，等待后续完善项目后进行文档完善

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
