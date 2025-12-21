plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {
    implementation(project(":kot-module-system:kot-module-system-server"))
}