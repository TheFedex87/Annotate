plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.core"
}

dependencies {
    room()
    hilt()

    // Projects
}