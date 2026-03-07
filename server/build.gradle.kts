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
    //ktor logback
    implementation(libs.ktor.server.logback)
    testImplementation(libs.ktor.server.testhost)
    testImplementation(libs.kotlin.test.junit)

    // firebase
    implementation("com.google.firebase:firebase-admin:9.2.0")
}