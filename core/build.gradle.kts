plugins {
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "it.thefedex87.core"
}

dependencies {
    implementation(Room.runtime)
    kapt(Room.compiler)
    implementation(Room.ktx)
    implementation(Compose.runtime)

    implementation(project(Modules.utils))
}