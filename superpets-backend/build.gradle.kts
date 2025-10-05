plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "1.9.10"
}

group = "com.alicankorkmaz"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

// Configure Ktor's shadow jar to merge service files properly for gRPC
ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

// This is CRITICAL for gRPC to work - must merge META-INF/services files
tasks.shadowJar {
    mergeServiceFiles()
    mergeServiceFiles("META-INF/services/io.grpc.LoadBalancerProvider")
    mergeServiceFiles("META-INF/services/io.grpc.NameResolverProvider")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback.classic)
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.stripe:stripe-java:25.0.0")

    // PostgreSQL + Exposed ORM (replaces Firestore)
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("org.jetbrains.exposed:exposed-core:0.44.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // JWT for Supabase auth token verification
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
