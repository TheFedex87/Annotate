plugins {
    `android-library`
    `kotlin-android`
    //id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.alarms_data"
}

dependencies {
    hilt()

    // Projects
    alarmsDomain()
    core()
    errorHandling()
    logging()
}