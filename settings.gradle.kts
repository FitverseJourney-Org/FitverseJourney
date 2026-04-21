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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
    ":presentation",
    ":server",
)
include(":data:local", ":data:remote", ":data:repository")
project(":data:local").projectDir      = File(rootDir, "data/local")
project(":data:remote").projectDir     = File(rootDir, "data/remote")
project(":data:repository").projectDir = File(rootDir, "data/repository")
