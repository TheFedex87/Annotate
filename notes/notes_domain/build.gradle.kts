plugins {
    `android-library`
    `kotlin-android`
}

dependencies {

}

android {
    namespace = "it.thefedex87.notes_domain"
}

dependencies {
    implementation(project(Modules.core))
}