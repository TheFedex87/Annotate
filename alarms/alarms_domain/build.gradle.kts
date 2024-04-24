plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.alarms_domain"
}

dependencies {
    hilt()

    // Projects
    core()
    errorHandling()
}