import org.gradle.api.initialization.resolve.RepositoriesMode

rootProject.name = "kot-cloud"

// Framework modules
include(
    ":kot-framework",
    ":kot-framework:kot-common-api",
    ":kot-framework:kot-common-model",
    ":kot-framework:kot-web"
)
// Gateway modules
include(
    ":kot-gateway",
//    ":kot-module-gateway:kot-module-gateway-api",
//    ":kot-module-gateway:kot-module-gateway-server"
)
// Module System sub-modules
include(
    ":kot-module-system",
    ":kot-module-system:kot-module-system-api",
    ":kot-module-system:kot-module-system-server"
)
// Main Entry
include(":kot-server")

//include(":service-hello")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://mirrors.huaweicloud.com/repository/maven/")
        mavenCentral()
        google()
    }
    plugins {
        kotlin("jvm") version "2.3.0"
        kotlin("kapt") version "2.3.0"
        id("com.google.devtools.ksp") version "2.3.4"
    }
}
