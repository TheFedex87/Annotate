import org.gradle.api.artifacts.dsl.DependencyHandler

object Compose {
    private const val activityComposeVersion = "1.8.2"
    const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"

    private const val bomVersion = "2024.01.00"
    const val bom = "androidx.compose:compose-bom:$bomVersion"
    const val ui = "androidx.compose.ui:ui"
    const val uiGraphics = "androidx.compose.ui:ui-graphics"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val material3 = "androidx.compose.material3:material3"
    const val iconExtended = "androidx.compose.material:material-icons-extended"
    const val uiTooling = "androidx.compose.ui:ui-tooling"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-compose:2.6.2"  // Dependency for collectAsStateWithLifecycle

    // Not needed since we are using hilt-navigation-compose
    // const val navigation = "androidx.navigation:navigation-compose:2.6.0"

    const val composeCompilerVersion = "1.5.8"
    const val compiler = "androidx.compose.compiler:compiler:$composeCompilerVersion"

    private const val hiltNavigationComposeVersion = "1.1.0"
    const val hiltNavigationCompose =
        "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"

    private const val version = "2.5.0"
    const val coilCompose = "io.coil-kt:coil-compose:$version"
}


fun DependencyHandler.coil() {
    implementation(Compose.coilCompose)
}

fun DependencyHandler.compose() {
    implementation(platform(Compose.bom))
    implementation(Compose.compiler)
    implementation(Compose.ui)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.material3)
    implementation(Compose.iconExtended)
    implementation(Compose.activityCompose)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.runtime)
}