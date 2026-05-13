plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)

    alias(libs.plugins.sqldelight)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.example.local"
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

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataLocalModule"
            isStatic = true
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        all { languageSettings.optIn("kotlin.time.ExperimentalTime") }

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(project(":domain"))

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            // SQLDelight isolado — coroutines-extensions seguro de ativar agora
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions) // estava comentado no original

            // DataStore (persistência local)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)

            implementation("com.russhwolf:multiplatform-settings:1.3.0")

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.sqldelight.android.driver)

            // EncryptedSharedPreferences
            implementation("androidx.security:security-crypto:1.1.0-alpha06")
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.journey.database.AppDatabase")
            srcDirs.setFrom("src/commonMain/sqldelight")
            generateAsync.set(true)
            schemaOutputDirectory.set(file("src/commonMain/sqldelight"))
            deriveSchemaFromMigrations.set(false)
        }
    }
    linkSqlite.set(true)
}