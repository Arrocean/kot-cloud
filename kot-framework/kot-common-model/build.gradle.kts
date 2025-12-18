plugins {
    kotlin("jvm")
}

version = "0.0.1"
group = "com.whitesprite.dev"

dependencies {
    // Micronaut
    implementation(libs.micronaut.validation)
    implementation(libs.micronaut.core)
    implementation(libs.micronaut.http.client)
    implementation(libs.micronaut.http)
    implementation(libs.micronaut.context)
}
