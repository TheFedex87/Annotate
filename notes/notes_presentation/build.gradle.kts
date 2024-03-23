plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.notes_presentation"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
}

dependencies {
    compose()
    hilt()

    // Project
    coreUi()
    core()
    notesDomain()
    notesUtils()
    errorHandling()
}