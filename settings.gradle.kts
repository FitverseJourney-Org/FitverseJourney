rootProject.name = "FitverseJourneyApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // ESTA LINHA É OBRIGATÓRIA
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // Adicione este se estiver usando versões Alpha/Snapshot da Koin
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
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