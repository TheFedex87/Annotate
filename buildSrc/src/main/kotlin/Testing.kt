import org.gradle.api.artifacts.dsl.DependencyHandler

object Testing {
    private const val junitVersion = "4.13.2"
    const val junit4 = "junit:junit:$junitVersion"

    private const val junitAndroidExtVersion = "1.1.5"
    const val junitAndroidExt = "androidx.test.ext:junit:$junitAndroidExtVersion"

    private const val espressoVersion = "3.5.1"
    const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"

    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4"

    private const val coroutineTestVersion = "1.6.4"
    const val coroutinesVersion = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineTestVersion"

    const val hiltTesting = "com.google.dagger:hilt-android-testing:${DaggerHilt.hiltVersion}"
}

fun DependencyHandler.test() {
    testImplementation(Testing.junit4)
    testImplementation(Testing.junitAndroidExt)
    testImplementation(Testing.coroutinesVersion)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Testing.junit4)
    androidTestImplementation(Testing.junitAndroidExt)
    androidTestImplementation(Testing.espresso)
    androidTestImplementation(Testing.composeUiTestJunit4)
    androidTestImplementation(Testing.hiltTesting)
}