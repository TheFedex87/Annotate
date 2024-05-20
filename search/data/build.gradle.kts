plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply<MainGradlePlugin>()

android {
    namespace = "it.thefedex87.search.data"
}

dependencies {
    dataStore()
    hilt()

    searchDomain()
    core()
    errorHandling()
}