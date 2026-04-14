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
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

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

            implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")


        }
        iosMain.dependencies {

            implementation(libs.ktor.client.darwin)
            // sqlDelight
            implementation(libs.sqldelight.native.driver)
        }
        androidMain.dependencies {

            // coroutines
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.kotlinx.coroutines.play.services)
            // firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth.ktx)
            implementation(libs.firebase.firestore)

            // koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)

            // engine ktor
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.android)

            // sqlDelight
            implementation(libs.sqldelight.android.driver)



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
        create("AppDatabase") {
            packageName.set("com.journey.database")
            generateAsync.set(true)
            schemaOutputDirectory.set(file("src/commonMain/sqldelight"))
            deriveSchemaFromMigrations.set(true)
        }
    }
    linkSqlite.set(true)
}