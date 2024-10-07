plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.brandevsolutions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.junrar:junrar:7.5.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}