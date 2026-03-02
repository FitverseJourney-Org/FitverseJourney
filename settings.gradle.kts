rootProject.name = "FitverseJourneyApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        // resolução de plugins
        gradlePluginPortal()
        google()
        mavenCentral()

        // necessário para Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    // força centralização de repositórios
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()

        // Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(
    ":androidApp",
    ":composeApp",
    ":domain",
    ":data",
    ":presentation",
    ":server"
)