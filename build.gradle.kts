plugins {
    kotlin("jvm") version "2.3.20" apply false
    id("com.google.devtools.ksp") version "2.3.6" apply false
    kotlin("plugin.allopen") version "2.3.20" apply false
    id("io.micronaut.application") version "4.6.2" apply false
    id("org.graalvm.buildtools.native") version "0.11.5" apply false
}

tasks.wrapper {
    gradleVersion = "9.4.1"
    distributionType = Wrapper.DistributionType.ALL
    distributionUrl = "https://mirrors.cloud.tencent.com/gradle/gradle-${gradleVersion}-all.zip"
}

val commonRepositories: RepositoryHandler.() -> Unit = {
    maven("https://mirrors.huaweicloud.com/repository/maven/")
    mavenCentral()
    google()
}

allprojects {
    repositories(commonRepositories)
}
