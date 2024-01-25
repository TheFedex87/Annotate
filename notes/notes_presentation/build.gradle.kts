plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/compose-module.gradle")

android {
    namespace = "it.thefedex87.notes_presentation"
}

dependencies {
    implementation(project(Modules.coreUi))
    implementation(project(Modules.notesDomain))
    implementation(project(Modules.utils))
}