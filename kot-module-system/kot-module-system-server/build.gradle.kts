plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("kapt")
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    // Micronaut Data
    implementation(libs.micronaut.data.runtime)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.micronaut.data.tx)
    // Micronaut Data Processor
    kapt(libs.micronaut.data.processor)
    // JDBC
    implementation(libs.micronaut.jdbc.hikari)
    // Postgre SQL
    runtimeOnly(libs.postgresql)
    // Micronaut 注解处理器
//    kapt(libs.micronaut.inject.java)

    // system-api 模块
    implementation(project(":kot-module-system:kot-module-system-api"))
    // kot-common-model 模块
    implementation(project(":kot-framework:kot-common-model"))
}