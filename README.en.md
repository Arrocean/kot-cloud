# kot-cloud

#### Introduction
This project aims to attempt building an enterprise-level project backend scaffolding using Kotlin as the basic language. The construction process is also a learning process for me to learn Kotlin

#### Description
This project aims to build an enterprise-level microservices scaffolding based on Kotlin, with the goal of functionally aligning with the SpringCloudAlibaba + SpringBoot + Maven + MybatisPlus + JVM ecosystem. At the same time, it leverages the features of Kotlin and GraalVM to maximize CPU efficiency and optimize memory usage.

The project is positioned as an enterprise-level template and reference implementation, encompassing commonly used module divisions (public tools, databases, web tier, management-side services, and optional gateways), facilitating direct reuse or serving as a foundation for secondary development in production-level projects.

Since this project is currently a long-term part of my learning projects, it will attempt a more aggressive technology selection plan in terms of functionality. Adjustments and optimizations may be made during the subsequent development process to ensure that the final scaffolding output meets the needs of enterprise-level projects, while also fully leveraging the advantages of Kotlin and GraalVM.

#### Software architecture
Software architecture description

This project is dedicated to building a modern, high-performance microservices architecture system, with the following technical choices:

**Core Language**:
- [Kotlin](https://kotlinlang.org/) 2.3.20 - As the primary development language, fully leverage its features such as null safety, extension functions, and coroutines to enhance development efficiency and code quality

**Core Framework**:
- [Micronaut](https://micronaut.io/) - This framework was chosen for its quick start and low memory footprint, making it particularly suitable for microservices and cloud-native applications. Compared to Spring Boot, it offers significant performance advantages

**Build Tool**:
- [Gradle](https://gradle.org/) - Uses Kotlin DSL as the build script language, providing better IDE support and type safety

**Compilation optimization**:
- [GraalVM](https://www.graalvm.org/) - used to compile applications into native images, significantly reducing startup time and memory consumption, and fully leveraging the advantages of Kotlin

**ORM**: Mixed usage
- [Micronaut Data](https://micronaut-projects.github.io/micronaut-data/latest/guide/) - used for the data access layer, providing type-safe ORM capabilities
- [Exposed](https://github.com/JetBrains/Exposed) - Used for relational database access, providing a type-safe SQL builder

database
- [PostgreSQL](https://www.postgresql.org/) - for relational databases
- [MariaDB](https://mariadb.org/) - used for relational databases (with future support)

**Plugin**:
- [KSP](https://github.com/google/ksp) 2.3.6 - used for code generation, providing faster compilation speed and better Kotlin support
- [Micronaut Application](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/) 4.6.2 - Used for creating and managing Micronaut applications

#### Installation tutorial

1. Currently, there is no content available. We will improve the documentation after further refining the project

#### Instructions for Use

1. Currently, there is no content. We will improve the documentation after the project is further developed

#### Participate in contributing

1. Fork this repository
2. Create a new Feat_xxx branch
3. Submit code
4. Create a new Pull Request