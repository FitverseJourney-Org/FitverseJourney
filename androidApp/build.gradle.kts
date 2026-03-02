plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
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
}

dependencies {

    implementation(projects.data)
    implementation(projects.presentation)
    implementation(projects.domain)
    implementation(projects.composeApp)


    // koin plugin
    api(libs.koin.core)
    implementation(libs.koin.compose)



    // datastore
    implementation(libs.datastore.androidx.preferences)
    implementation(libs.datastore.androidx)

    api(project.dependencies.platform(libs.compose.bom))
    implementation(libs.navigation.compose)
    debugImplementation(libs.compose.androidx.ui.tooling)
    debugImplementation(libs.compose.androidx.ui.tooling.preview)

    // Import the Firebase BoM
    api(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)

    // Compose
    implementation(libs.compose.androidx.ui)
    implementation(libs.compose.androidx.ui.tooling.preview)
    implementation(libs.compose.androidx.ui.graphics)
    implementation(libs.compose.androidx.material3)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}