plugins {
    `android-library`
    `kotlin-android`
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
}