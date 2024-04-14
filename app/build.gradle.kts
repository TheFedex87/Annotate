plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

apply<MainGradlePlugin>()

android {
    namespace = ProjectConfig.appId

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    room()
    compose()
    hilt()


    // Projects
    coreUi()
    core()
    notesPresentation()
    notesDomain() // For DI purpose
    notesData() // For DI purpose

    calendarPresentation()
}