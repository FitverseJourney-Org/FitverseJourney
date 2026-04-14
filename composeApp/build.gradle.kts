plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}


kotlin {
    androidLibrary {
        namespace = "com.fitverse.project.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    sourceSets {

        androidMain.dependencies {

            // -----------------------------------------------------------------------------------------
            // DEPENDENCY INJECTION
            // ----------------------------------------------------------------------------------------
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

        }

        iosMain.dependencies {
        }

        commonMain.dependencies {
            implementation(projects.data)
            // -----------------------------------------------------------------------------------------
            // PROJECT MODULES
            // -----------------------------------------------------------------------------------------
            implementation(projects.presentation)
            implementation(projects.domain)

            // -----------------------------------------------------------------------------------------
            // KOTLIN / CORE
            // -----------------------------------------------------------------------------------------
            implementation(libs.kotlin.stdlib)

            // -----------------------------------------------------------------------------------------
            // MATERIAL 3
            // -----------------------------------------------------------------------------------------
            api(libs.compose.material3)

            // -----------------------------------------------------------------------------------------
            // ICONS
            // -----------------------------------------------------------------------------------------
            implementation(libs.icons.compose.material)


            // -----------------------------------------------------------------------------------------
            // COMPOSE MULTIPLATFORM
            // -----------------------------------------------------------------------------------------
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)

            // -----------------------------------------------------------------------------------------
            // NAVIGATION
            // -----------------------------------------------------------------------------------------
            implementation(libs.jetbrains.navigation3.ui)

            // -----------------------------------------------------------------------------------------
            // LIFECYCLE / VIEWMODEL
            // -----------------------------------------------------------------------------------------
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.navigation3)

            // -----------------------------------------------------------------------------------------
            // DEPENDENCY INJECTION
            // -----------------------------------------------------------------------------------------
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

            // -----------------------------------------------------------------------------------------
            // SERIALIZATION
            // -----------------------------------------------------------------------------------------
            implementation(libs.kotlinx.serialization)

            // -----------------------------------------------------------------------------------------
            // RESOURCES
            // -----------------------------------------------------------------------------------------
            implementation(libs.components.resources)

        }

        commonTest.dependencies {

        }
    }
}
