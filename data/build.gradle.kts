plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlinSerialization)

    // sqlDelight
    alias(libs.plugins.sqldelight)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.example.data"
        compileSdk = 36
        minSdk = 26

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataModule"
            isStatic = true
        }
    }


    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(project(":domain"))

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // datastore
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            // sqldelight
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
            
            implementation(libs.androidx.compose.runtime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.auth)
        }
        iosMain.dependencies {

            // sqlDelight
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
        androidMain.dependencies {


            // firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.android.firebase.auth)
            implementation(libs.android.firebase.analytics)

            // engine ktor
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)

            // sqlDelight
            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)

            // koin android
            implementation(libs.koin.android)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }
    }

}

sqldelight {
    databases {
        create("FitverseDatabase") {
            packageName.set("com.fitverse.database")
            verifyMigrations.set(false)
        }
    }
}