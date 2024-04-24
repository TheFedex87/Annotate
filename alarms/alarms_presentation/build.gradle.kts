plugins {
    `android-library`
    `kotlin-android`
    //id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.alarms_presentation"

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
    alarmsDomain()
    errorHandling()
    logging()

    //testWithJUnit5()
    //mockito()
}