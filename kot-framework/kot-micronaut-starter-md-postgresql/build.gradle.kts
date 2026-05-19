plugins {
    kotlin("jvm")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    api(libs.postgresql)
    api(project(":kot-framework:kot-micronaut-starter-md-core"))
}
