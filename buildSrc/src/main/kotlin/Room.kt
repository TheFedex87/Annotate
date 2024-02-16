import org.gradle.api.artifacts.dsl.DependencyHandler

object Room {
    private const val version = "2.6.1"
    const val runtime = "androidx.room:room-runtime:$version"
    const val compiler = "androidx.room:room-compiler:$version"
    const val roomKtx = "androidx.room:room-ktx:$version"
}

fun DependencyHandler.room() {
    implementation(Room.runtime)
    implementation(Room.roomKtx)
    kapt(Room.compiler)
}