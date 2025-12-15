rootProject.name = "kot-cloud"

// Framework modules
include(
    ":kot-framework",
    ":kot-framework:kot-common-api"
)
//include(":service-hello")

pluginManagement {
    repositories {
        maven("https://mirrors.huaweicloud.com/repository/maven/")
    }
}