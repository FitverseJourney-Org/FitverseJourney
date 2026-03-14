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
            implementation(libs.compose.foundation)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)

            implementation(libs.compose.material3)

            implementation(project(":domain"))

            // koin
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
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.android.lottie.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            // preview
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.ui.tooling)
        }
    }
}
