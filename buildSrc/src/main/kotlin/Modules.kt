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
    const val calendarDomain = ":calendar:calendar_domain"
    const val calendarData = ":calendar:calendar_data"
    const val calendarPresentation = ":calendar:calendar_presentation"
    const val logging = ":logging"
    const val alarmsDomain = ":alarms:alarms_domain"
    const val alarmsData = ":alarms:alarms_data"
    const val alarmsPresentation = ":alarms:alarms_presentation"
    const val searchDomain = ":search:domain"
    const val searchData = ":search:data"
    const val searchPresentation = ":search:presentation"
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

fun DependencyHandler.calendarData() {
    implementation(project(Modules.calendarData))
}

fun DependencyHandler.calendarDomain() {
    implementation(project(Modules.calendarDomain))
}

fun DependencyHandler.calendarPresentation() {
    implementation(project(Modules.calendarPresentation))
}

fun DependencyHandler.logging() {
    implementation(project(Modules.logging))
}

fun DependencyHandler.alarmsDomain() {
    implementation(project(Modules.alarmsDomain))
}

fun DependencyHandler.alarmsData() {
    implementation(project(Modules.alarmsData))
}

fun DependencyHandler.alarmsPresentation() {
    implementation(project(Modules.alarmsPresentation))
}

fun DependencyHandler.searchData() {
    implementation(project(Modules.searchData))
}

fun DependencyHandler.searchPresentation() {
    implementation(project(Modules.searchPresentation))
}

fun DependencyHandler.searchDomain() {
    implementation(project(Modules.searchDomain))
}
