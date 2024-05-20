plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.search.domain"
}

dependencies {
    hilt()
    core()
    errorHandling()
}