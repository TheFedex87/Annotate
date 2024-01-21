plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/compose-module.gradle")

android {
    namespace = "it.thefedex87.notes_presentation"
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.notesDomain))
}