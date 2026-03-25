plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")

    // Micronaut 模块
    id("io.micronaut.application")
//
//    // GraalVM 模块
    id("org.graalvm.buildtools.native")
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    // Micronaut Runtime
    implementation(libs.micronaut.runtime)
    // Micronaut HTTP
    implementation(libs.micronaut.http.server.netty)

    // Micronaut Data Processor
    ksp(libs.micronaut.inject.kotlin)
    ksp(libs.micronaut.jackson.processor)

    // Kotlin 友好日志 API（编译期需要）
    implementation(libs.kotlin.logging)

    // SLF4J -> Log4j2（运行时绑定）
    runtimeOnly(libs.log4j.slf4j2.impl)
    runtimeOnly(libs.log4j.core)
    runtimeOnly(libs.log4j.api)

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
}

ksp {
    arg("micronaut.processing.incremental", "true")
    arg("micronaut.processing.annotations", "com.whitesprite.dev.*")
}

application {
    mainClass.set("com.whitesprite.dev.server.KotlinServerApplication")
}

micronaut {
    // Micronaut Platform 4.10.7
    version.set("4.10.7")
    importMicronautPlatform.set(true)

    runtime("netty")

    processing {
        incremental(true)
        // 默认使用**
        annotations("com.whitesprite.dev.**")
    }
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("kot-server")
            mainClass.set("com.whitesprite.dev.server.KotlinServerApplication")
            buildArgs.addAll(
                "-J-Xmx${project.property("native.image.xmx")}",
                "-J-Xms${project.property("native.image.xms")}",
                "--no-fallback"
            )
        }
    }
}