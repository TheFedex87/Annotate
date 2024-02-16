import org.gradle.api.artifacts.dsl.DependencyHandler

object Build {
    private const val hiltAndroidGradlePluginVersion = DaggerHilt.hiltVersion
    const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltAndroidGradlePluginVersion"

    private const val desugaringVersion = "2.0.4"
    const val desugaring = "com.android.tools:desugar_jdk_libs_nio:$desugaringVersion"
}

fun DependencyHandler.desugaring() {
    coreLibraryDesugaring(Build.desugaring)
}

