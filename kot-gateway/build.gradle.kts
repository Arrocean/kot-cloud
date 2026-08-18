plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    implementation(platform(libs.micronaut.platform))
    implementation(libs.micronaut.runtime)
    implementation(libs.micronaut.http)
    implementation(libs.micronaut.http.client)
    implementation(libs.micronaut.http.server.netty)
    implementation(libs.micronaut.serde.jackson)
    implementation(libs.micronaut.jackson.databind)
    implementation(libs.micronaut.security.jwt)
    implementation(libs.micronaut.redis.lettuce)
    implementation(libs.kotlin.logging)
    runtimeOnly(libs.slf4j.simple)

    implementation(project(":kot-framework:kot-common-model"))

    ksp(libs.micronaut.inject.kotlin)
    ksp(libs.micronaut.jackson.processor)

    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(libs.junit.jupiter.engine)
}

ksp {
    arg("micronaut.processing.incremental", "true")
    arg("micronaut.processing.annotations", "com.arrocean.dev.gateway.**")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("com.arrocean.dev.gateway.GatewayApplication")
}
