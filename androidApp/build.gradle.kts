plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
    id("org.jetbrains.compose")
    // ❌ REMOVA: kotlin("android") — não é mais necessário com AGP 9.0
}

android {
    namespace = "org.fitverse.fitverseJourney"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.fitverse.fitverseJourney"  // 👈 obrigatório
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-DEBUG"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // ❌ REMOVA o segundo bloco release duplicado que estava antes
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        // ❌ REMOVA o buildFeatures duplicado que estava antes
    }
}

dependencies {

    // -----------------------------------------------------------------------------------------
    // MODULES
    // -----------------------------------------------------------------------------------------
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))
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
    // SETTINGS
    // -----------------------------------------------------------------------------------------
    implementation("com.russhwolf:multiplatform-settings:1.3.0")


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