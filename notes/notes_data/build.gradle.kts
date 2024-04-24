plugins {
    `android-library`
    `kotlin-android`
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.notes_data"
}

dependencies {
    dataStore()
    hilt()

    // Projects
    notesDomain()
    notesUtils()
    core()
    errorHandling()
    logging()
    alarmsDomain()

    testWithJUnit5()
    mockito()
}