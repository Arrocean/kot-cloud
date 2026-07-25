plugins {
    application
    kotlin("jvm")
    // GraalVM 模块
    id("org.graalvm.buildtools.native")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    // Micronaut Platform
    implementation(platform(libs.micronaut.platform))
    // Micronaut Runtime
    implementation(libs.micronaut.runtime)
    // Micronaut HTTP
    implementation(libs.micronaut.http.server.netty)
    implementation(libs.micronaut.tracing.opentelemetry)

    // Kotlin 友好日志 API（编译期需要）
    implementation(libs.kotlin.logging)

    // SLF4J Simple（轻量控制台后端）
    runtimeOnly(libs.slf4j.simple)

    // Micronaut Jackson
    implementation(libs.micronaut.serde.jackson)
    implementation(libs.micronaut.jackson.databind)

    /* ============= 基础设施 Starter（关键） ============= */
    // 选择你的数据库组合：PostgreSQL (包含 md-jdbc-core + driver)
    implementation(project(":kot-framework:kot-micronaut-starter-md-postgresql"))
    /* ============= 业务模块 ============= */
    // System 模块
    implementation(project(":kot-module-system:kot-module-system-server"))
}

configurations.configureEach {
    exclude(group = "ch.qos.logback", module = "logback-classic")
    exclude(group = "ch.qos.logback", module = "logback-core")
    exclude(group = "org.apache.logging.log4j", module = "log4j-core")
    exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j2-impl")
}

application {
    mainClass.set("com.arrocean.dev.server.KotlinServerApplication")
}

graalvmNative {
    toolchainDetection.set(true)

    binaries {
        named("main") {
            imageName.set("kot-server")
            mainClass.set("com.arrocean.dev.server.KotlinServerApplication")
            buildArgs.addAll(
                "-J-Xmx${project.property("native.image.xmx")}",
                "-J-Xms${project.property("native.image.xms")}",
                "-H:+EnableFallbackCompilation"
            )
        }
    }
}
