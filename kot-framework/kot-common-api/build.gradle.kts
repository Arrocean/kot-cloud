plugins {
    kotlin("jvm")
}

version = "0.1"
group = "com.whitesprite.dev"

kotlin {
    // TODO WhiteSprite：用于修复目前 Kotlin 2.2.21 不支持 Java 25 的问题；预计 12月16日 Kotlin 2.3 更新后跟进
    jvmToolchain(24)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

dependencies {
    api(project(":kot-framework:kot-common-model"))

//    api(libs.micronaut.http)
}
