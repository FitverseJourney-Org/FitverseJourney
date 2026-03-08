plugins {
    alias(libs.plugins.kotlinJvm) // Add this plugin
    alias(libs.plugins.ktor)
    application // Add this plugin
}

group = "org.fitverse.project"
version = "1.0.0"
application {
    mainClass.set("org.fitverse.project.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    // ktor logback
    implementation(libs.ktor.server.logback)
    testImplementation(libs.ktor.server.testhost)
    testImplementation(libs.kotlin.test.junit)

    // firebase
    implementation(libs.firebase.admin)

    // coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)

    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.4.1")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.4.1")
    implementation("io.ktor:ktor-server-status-pages-jvm:3.4.1")
    implementation("io.ktor:ktor-server-cors-jvm:3.4.1")

    // serializable
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)

}