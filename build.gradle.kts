import org.gradle.api.artifacts.dsl.RepositoryHandler

val commonRepositories: RepositoryHandler.() -> Unit = {
    maven("https://mirrors.huaweicloud.com/repository/maven/")
    mavenCentral()
    google()
}

allprojects {
    repositories(commonRepositories)
}
