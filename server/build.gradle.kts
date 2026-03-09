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

    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------- FIREBASE ----------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    implementation(libs.firebase.admin)
    implementation(libs.firebase.firestore.ktx)

    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------- COROUTINES ----------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)

    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------- PLUGINS KTOR ----------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.server.call.logging.jvm)
    implementation(libs.ktor.server.status.pages.jvm)
    implementation(libs.ktor.server.cors.jvm)

    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------- SERIALIZABLE ----------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)

    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------- KOIN ----------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    implementation(libs.koin.ktor)
    implementation(libs.koin.ktor.logger)

}
