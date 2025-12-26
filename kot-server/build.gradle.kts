plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    // Micronaut Runtime
    implementation(libs.micronaut.runtime)
    // Micronaut HTTP
    implementation(libs.micronaut.http.server.netty)
    // Micronaut Data Processor
    ksp(libs.micronaut.data.processor)
    ksp(libs.micronaut.inject.kotlin)
    // SLF4J 相关
    runtimeOnly(libs.logback.classic)
    // Micronaut Jackson
    implementation(libs.micronaut.jackson)
    implementation(libs.micronaut.jackson.databind)

    /* ============= 业务模块 ============= */
    // System 模块
    implementation(project(":kot-module-system:kot-module-system-server"))
}

ksp {
    arg("micronaut.processing.incremental", "true")
    arg("micronaut.processing.annotations", "com.whitesprite.dev.*")
}