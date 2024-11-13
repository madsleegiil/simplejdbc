plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.madslee"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    testImplementation(kotlin("test"))
    testImplementation("io.zonky.test:embedded-postgres:2.0.7")
    testImplementation("org.flywaydb:flyway-database-postgresql:10.21.0")
    testImplementation("org.postgresql:postgresql:42.7.4")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
