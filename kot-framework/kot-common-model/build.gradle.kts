plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

version = "0.0.1"
group = "com.arrocean.dev"

dependencies {
    api(libs.jakarta.validation)
    api(libs.micronaut.http)
    compileOnly(libs.micronaut.serde.jackson)

    ksp(libs.micronaut.jackson.processor)
    ksp(libs.micronaut.inject.kotlin)
}

