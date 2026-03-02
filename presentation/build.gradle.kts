plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    androidLibrary {
        namespace = "com.example.presentation"
        compileSdk = 36
        minSdk = 26

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "PresentationModule"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            // compose
            implementation(libs.compose.androidx.foundation)
            implementation(libs.compose.androidx.runtime)
            // material3
            implementation(libs.material3)

            implementation(project(":domain"))

            // koin (ok em commonMain)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // icons
            implementation(libs.icons.compose.material)

            // components resources
            implementation(libs.components.resources)

            // coroutines
            implementation(libs.kotlinx.coroutines.core)

            // dateTime
            implementation(libs.kotlinx.datetime)
        }
        iosMain.dependencies {
        }
        androidMain.dependencies {
            // lifecycle (mover para androidMain)
            implementation(libs.lifecycle.viewmodel)

            // lottie
            implementation(libs.android.lottie.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
    }

}