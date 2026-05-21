plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    // sem sqldelight, sem serialization — só orquestra
}

kotlin {
    androidLibrary {
        namespace = "org.fitverse.data.repository"
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
            baseName = "DataRepositoryModule"
            isStatic = true
        }
    }

    sourceSets {
        all { languageSettings.optIn("kotlin.time.ExperimentalTime") }

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(project(":domain"))
            implementation(project(":data:local"))   // acessa LocalDataSources
            implementation(project(":data:remote"))  // acessa RemoteDataSources

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            // DataStore (DataStore<Preferences>)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
        }
    }
}