pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Annotate"
include(":app")
include(":core")
include(":notes")
include(":notes:notes_presentation")
include(":notes:notes_domain")
include(":core_ui")
include(":utils")
include(":notes:notes_utils")
include(":notes:notes_data")
