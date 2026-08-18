plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    api(libs.postgresql.r2dbc)
    api(project(":kot-framework:kot-micronaut-starter-md-core"))
}