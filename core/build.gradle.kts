plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "it.thefedex87.core"
}