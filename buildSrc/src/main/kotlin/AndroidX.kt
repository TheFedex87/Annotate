import org.gradle.api.artifacts.dsl.DependencyHandler

object AndroidX {
    private const val coreKtxVersion = "1.12.0"
    const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"
}

fun DependencyHandler.coreKtx() {
    implementation(AndroidX.coreKtx)
}