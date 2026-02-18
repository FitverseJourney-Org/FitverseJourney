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
            implementation(project(":domain"))

            // koin dependencies

            // compose
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.runtime)
            implementation(libs.androidx.compose.components.resources)

            // material
            implementation(libs.compose.material.icons)
            implementation(libs.material3)

            // coroutines
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)



        }
        iosMain.dependencies {

        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            // lottie
            implementation(libs.android.lottie.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

}