plugins {
    `android-library`
    `kotlin-android`
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
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
    logging()

    testWithJUnit5()
    mockito()
}