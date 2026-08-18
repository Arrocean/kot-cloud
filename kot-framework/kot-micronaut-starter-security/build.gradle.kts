plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    // Micronaut Http
    implementation(libs.micronaut.http)
    implementation(libs.micronaut.http.server.netty)

    // Micronaut Jackson
    implementation(libs.micronaut.serde.jackson)
    ksp(libs.micronaut.jackson.processor)

    // Micronaut Security
    api(libs.micronaut.security)
    api(libs.micronaut.security.jwt)

    // Kotlin Logging API
    implementation(libs.kotlin.logging)

    // Password Hash
    implementation(libs.bcprov.jdk18on)
    implementation(libs.jbcrypt)
    implementation(libs.micronaut.redis.lettuce)

    // 注解注入
    implementation(libs.micronaut.runtime)
    implementation(libs.micronaut.context)
    ksp(libs.micronaut.inject.kotlin)

    // Micronaut Validation
    implementation(libs.micronaut.validation)

    // Common Model
    implementation(project(":kot-framework:kot-common-api"))
    api(project(":kot-framework:kot-common-model"))
}