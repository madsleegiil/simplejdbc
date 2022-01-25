import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

group = "com.madslee"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    testImplementation(kotlin("test"))
    testImplementation("com.zaxxer:HikariCP:4.0.2")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.flywaydb:flyway-core:7.5.3")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}