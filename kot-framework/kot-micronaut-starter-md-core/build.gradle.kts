plugins {
    kotlin("jvm")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    api(libs.micronaut.data.runtime)
    api(libs.micronaut.data.r2dbc)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactor)

    api(project(":kot-framework:kot-micronaut-starter-security"))
    api(project(":kot-framework:kot-common-model"))
}