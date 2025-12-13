plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
}

group = 'com.whitesprite.dev'
version = '0.0.1'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

test {
    useJUnitPlatform()
}