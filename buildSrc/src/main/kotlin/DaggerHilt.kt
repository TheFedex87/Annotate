import org.gradle.api.artifacts.dsl.DependencyHandler

object DaggerHilt {
    const val hiltVersion = "2.50"
    const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"
}

fun DependencyHandler.hilt() {
    kapt(DaggerHilt.hiltCompiler)
    implementation(DaggerHilt.hiltAndroid)
    kaptAndroidTest(DaggerHilt.hiltCompiler)
}