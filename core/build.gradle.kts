plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/base-module.gradle")
}

dependencies {

}

android {
    namespace = "it.thefedex87.core"
}