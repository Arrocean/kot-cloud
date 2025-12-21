plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    // Micronaut Runtime
    implementation(libs.micronaut.runtime)
    // Micronaut HTTP
    implementation(libs.micronaut.http.server.netty)

    /* ============= 业务模块 ============= */
    // System 模块
    implementation(project(":kot-module-system:kot-module-system-server"))
}