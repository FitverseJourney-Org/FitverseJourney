plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlinSerialization)  // ← único módulo com esse plugin
}

kotlin {
    androidLibrary {
        namespace = "com.example.data.remote"
        compileSdk = 36
        minSdk = 26
        withHostTestBuilder {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataRemoteModule"
            isStatic = true
        }
    }

    sourceSets {
        all { languageSettings.optIn("kotlin.time.ExperimentalTime") }

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(project(":domain"))

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            api(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            // Firebase + coroutines-play-services confinados aqui
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.analytics)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)

            // Ktor engine Android
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin) // veja nota abaixo
        }
    }
}