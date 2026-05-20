plugins {
    kotlin("jvm") version "2.3.21" apply false
    id("com.google.devtools.ksp") version "2.3.8" apply false
    kotlin("plugin.allopen") version "2.3.21" apply false
    id("io.micronaut.application") version "5.0.0" apply false
    id("org.graalvm.buildtools.native") version "0.11.5" apply false
}

tasks.wrapper {
    gradleVersion = "9.5.1"
    distributionType = Wrapper.DistributionType.ALL
    distributionUrl = "https://mirrors.cloud.tencent.com/gradle/gradle-${gradleVersion}-all.zip"
}

val commonRepositories: RepositoryHandler.() -> Unit = {
//    maven("https://mirrors.huaweicloud.com/repository/maven/")
//    maven("https://maven.aliyun.com/repository/public/")
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    mavenCentral()
    google()
}

allprojects {
    repositories(commonRepositories)
}
