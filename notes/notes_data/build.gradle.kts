plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/base-module.gradle")

android {
    namespace = "it.thefedex87.notes_data"
}

dependencies {
    implementation(DataStore.dataStore)
    implementation(project(Modules.notesDomain))
    implementation(project(Modules.notesUtils))
    implementation(project(Modules.core))
}