plugins {
    kotlin("jvm") version "2.3.10" apply false
    id("com.google.devtools.ksp") version "2.3.5" apply false
    kotlin("plugin.allopen") version "2.3.10" apply false
    id("io.micronaut.application") version "4.6.1" apply false
    id("org.graalvm.buildtools.native") version "0.11.3" apply false
}

val commonRepositories: RepositoryHandler.() -> Unit = {
    maven("https://mirrors.huaweicloud.com/repository/maven/")
    mavenCentral()
    google()
}

allprojects {
    repositories(commonRepositories)
}
