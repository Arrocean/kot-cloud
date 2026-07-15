plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.arrocean.dev"
version = "0.0.1"

dependencies {
    api(libs.postgresql)
    api(project(":kot-framework:kot-micronaut-starter-md-core"))

    implementation(libs.micronaut.runtime)
    implementation(libs.micronaut.context)
    ksp(libs.micronaut.inject.kotlin)
}
