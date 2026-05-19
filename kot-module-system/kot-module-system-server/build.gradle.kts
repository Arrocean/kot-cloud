plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    // Micronaut HTTP
    api(libs.micronaut.http.client)
    api(libs.micronaut.http.server.netty)

    // Kotlin Logging API
    implementation(libs.kotlin.logging)

    // Micronaut Validation
    api(libs.micronaut.validation)

    // Micronaut Data
    api(libs.micronaut.data.runtime)
    api(libs.micronaut.data.jdbc)
    api(libs.micronaut.data.tx)

    // Micronaut Data Processor
    ksp(libs.micronaut.data.processor)

    // Micronaut Jackson
    api(libs.micronaut.serde.jackson)
    api(libs.micronaut.jackson.databind)

    // JDBC
    api(libs.micronaut.jdbc.hikari)

    // Postgre SQL
    runtimeOnly(libs.postgresql)

    // Micronaut 注解处理器
    ksp(libs.micronaut.inject.kotlin)


    // system-api 模块
    api(project(":kot-module-system:kot-module-system-api"))
    // kot-common-api 模块
    api(project(":kot-framework:kot-common-api"))
    // kot-common-model 模块
    api(project(":kot-framework:kot-common-model"))

    // Starter
    // starter-web 模块
    api(project(":kot-framework:kot-micronaut-starter-web"))
    // starter-security 模块
    api(project(":kot-framework:kot-micronaut-starter-security"))
    // starter-md-core 模块
    api(project(":kot-framework:kot-micronaut-starter-md-core"))
    // starter-md-postgresql 模块
    api(project(":kot-framework:kot-micronaut-starter-md-postgresql"))
}
