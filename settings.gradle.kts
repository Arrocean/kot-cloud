rootProject.name = "kot-cloud"

// Framework modules
include(":kot-framework")
//include(":service-hello")

pluginManagement {
    repositories {
        maven("https://mirrors.huaweicloud.com/repository/maven/")
    }
}
include("kot-framework")