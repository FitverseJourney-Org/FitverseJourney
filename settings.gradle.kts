rootProject.name = "FitverseJorneyApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        // repos privados se necessário
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        // seu repositório privado:
        maven { url = uri("https://company/com/maven2") }
        // outros repositórios necessários...
    }

}

include(":composeApp", ":server", ":androidApp", ":domain", ":presentation", ":data")
