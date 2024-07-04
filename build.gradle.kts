
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10" // Ensure correct version

}

group = "example.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.6.7")
    implementation("io.ktor:ktor-server-netty:1.6.7")
    implementation("io.ktor:ktor-gson:1.6.7")
    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-json:1.6.7")
    implementation("io.ktor:ktor-client-logging:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation("io.ktor:ktor-serialization:1.7.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-client-serialization:1.6.7")
    implementation("io.ktor:ktor-server-config-yaml")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
    implementation("org.litote.kmongo:kmongo-coroutine:4.4.0")
    implementation("org.litote.kmongo:kmongo-serialization:4.4.0")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.1")
    implementation("org.mongodb:mongodb-driver-sync:4.3.3")
    implementation("redis.clients:jedis:3.7.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.mockk:mockk:1.12.0") // Replace with the latest version

    // Testing dependencies
    testImplementation("io.ktor:ktor-server-tests:2.1.0")
    testImplementation("io.ktor:ktor-server-test-host:1.6.21")
    testImplementation("io.ktor:ktor-client-mock:1.6.21")
    testImplementation("io.ktor:ktor-client-mock:1.6.21")
    testImplementation("io.ktor:ktor-client-mock:1.6.21")
    testImplementation("io.ktor:ktor-client-mock:1.6.21")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.ktor:ktor-server-tests-jvm")

    testImplementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.21")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.21")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
    implementation("io.dapr:dapr-sdk:1.0.0") // Use the latest version available


    testImplementation("io.ktor:ktor-server-tests:2.1.0")
    testImplementation("io.ktor:ktor-server-test-host:1.6.21")
    testImplementation("io.ktor:ktor-client-mock:1.6.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    testImplementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit5")
    }
}
tasks.test {
    useJUnitPlatform()
}
