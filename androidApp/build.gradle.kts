plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
    id("org.jetbrains.compose")
}

android {
    namespace = "org.fitverse.fitverseJourney"
    compileSdk = 36
    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-DEBUG"
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // -----------------------------------------------------------------------------------------
    // MODULES
    // -----------------------------------------------------------------------------------------
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)
    implementation(projects.composeApp)

    // -----------------------------------------------------------------------------------------
    // KOIN
    // -----------------------------------------------------------------------------------------
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)


    // -----------------------------------------------------------------------------------------
    // DATASTORE
    // -----------------------------------------------------------------------------------------
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    // -----------------------------------------------------------------------------------------
    // FIREBASE
    // -----------------------------------------------------------------------------------------
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // -----------------------------------------------------------------------------------------
    // COMPOSE
    // -----------------------------------------------------------------------------------------

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.graphics)

    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)

    // -----------------------------------------------------------------------------------------
    // ANDROIDX CORE
    // -----------------------------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // -----------------------------------------------------------------------------------------
    // TESTS
    // -----------------------------------------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}