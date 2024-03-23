import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Modules {
    const val core = ":core"
    const val coreUi = ":core_ui"

    const val notesDomain = ":notes:notes_domain"
    const val notesData = ":notes:notes_data"
    const val notesPresentation = ":notes:notes_presentation"
    const val notesUtils = ":notes:notes_utils"
    const val errorHandling = ":error_handling"
}

fun DependencyHandler.core() {
    implementation(project(Modules.core))
}

fun DependencyHandler.coreUi() {
    implementation(project(Modules.coreUi))
}

fun DependencyHandler.notesDomain() {
    implementation(project(Modules.notesDomain))
}

fun DependencyHandler.notesData() {
    implementation(project(Modules.notesData))
}

fun DependencyHandler.notesPresentation() {
    implementation(project(Modules.notesPresentation))
}

fun DependencyHandler.notesUtils() {
    implementation(project(Modules.notesUtils))
}

fun DependencyHandler.errorHandling() {
    implementation(project(Modules.errorHandling))
}