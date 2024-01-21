plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/base-module.gradle")

android {
    namespace = "it.thefedex87.notes_domain"
}

dependencies {
    implementation(project(Modules.core))
}