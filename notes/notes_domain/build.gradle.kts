plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.notes_domain"
}

dependencies {
    hilt()

    core()
}