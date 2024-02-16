import org.gradle.api.artifacts.dsl.DependencyHandler

object DataStore {
    private const val version = "1.0.0"
    const val dataStore = "androidx.datastore:datastore-preferences:$version"
}

fun DependencyHandler.dataStore() {
    implementation(DataStore.dataStore)
}