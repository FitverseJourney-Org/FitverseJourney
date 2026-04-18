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
            implementation(project.dependencies.platform(libs.koin.bom))
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


            // vico
            implementation(libs.vico.compose)
            implementation(libs.vico.compose.m3)


            // charts
            implementation ("io.github.ehsannarmani:compose-charts:0.2.5")


            // Coil
            implementation(libs.coil.compose)

        }
        iosMain.dependencies {
        }
        androidMain.dependencies {
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.android.lottie.compose)
            // preview
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.ui.tooling)
        }
    }
}
