rootProject.name = "kot-cloud"

// Framework modules
include(
    ":kot-framework",
    ":kot-framework:kot-common-api",
    ":kot-framework:kot-common-model",
    ":kot-framework:kot-web"
)
include(
    ":kot-module-system",
    ":kot-module-system:kot-module-system-api",
    ":kot-module-system:kot-module-system-server"
)
//include(":service-hello")

pluginManagement {
    repositories {
        maven("https://mirrors.huaweicloud.com/repository/maven/")
    }
}