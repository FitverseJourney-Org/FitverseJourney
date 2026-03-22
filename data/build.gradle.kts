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
            api(libs.koin.core)

            // datastore
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)

            // sqldelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.kotlinx.datetime)
            
            implementation(libs.compose.runtime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)

            // serialization
            implementation(libs.kotlinx.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        iosMain.dependencies {

            // sqlDelight
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        androidMain.dependencies {


            // firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth.ktx)
            implementation(libs.firebase.firestore)

            // engine ktor
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)

            // sqlDelight
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android.driver)


            // coroutines
            implementation(libs.kotlinx.coroutines.android)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.testExt.junit)
            }
        }
    }

}

sqldelight {
    databases {
        create("UserDatabase") {
            packageName.set("com.fitverse.database")
            verifyMigrations.set(false)
        }
    }
}