plugins {
    kotlin("jvm")
}

group = "com.whitesprite.dev"
version = "0.0.1"

dependencies {

    // =============== 框架依赖 =============
    // kot-common-model 模块
    api(project(":kot-framework:kot-common-model"))
}