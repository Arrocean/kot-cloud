plugins {
    kotlin("jvm")
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    api(libs.micronaut.data.runtime)
    implementation(libs.micronaut.data.jdbc)
    implementation(libs.micronaut.data.tx)
    implementation(libs.micronaut.jdbc.hikari)

    api(project(":kot-framework:kot-common-model"))
}