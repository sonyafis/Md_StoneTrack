pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.0" // версия плагина Kotlin
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // версия плагина Compose
        id("androidx.compose.compiler") version "1.5.10" // Плагин для компилятора Compose
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Md_StoneTrack"
include(":app")
 