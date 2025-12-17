plugins {
    kotlin("jvm")
}

version = "0.0.1"
group = "com.whitesprite.dev"
//
//kotlin {
//    jvmToolchain(24)
//}
//
//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(24))
//    }
//}

dependencies {
    // Micronaut
    api(libs.micronaut.core)
    api(libs.micronaut.validation)
    api(libs.micronaut.validation.annotations)
    implementation(libs.micronaut.http.client)
    implementation(libs.micronaut.http)
}
