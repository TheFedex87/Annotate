import org.gradle.api.artifacts.dsl.DependencyHandler

object Testing {
    private const val junit4Version = "4.13.2"
    const val junit4 = "junit:junit:$junit4Version"

    private const val junit5Version = "5.9.3"
    const val junit5 = "org.junit.jupiter:junit-jupiter-api:$junit5Version"
    const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:$junit5Version"
    const val junit5Params = "org.junit.jupiter:junit-jupiter-params:$junit5Version"

    private const val junitAndroidExtVersion = "1.1.5"
    const val junitAndroidExt = "androidx.test.ext:junit:$junitAndroidExtVersion"

    private const val espressoVersion = "3.5.1"
    const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"

    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4"

    private const val coroutineTestVersion = "1.6.4"
    const val coroutinesVersion = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineTestVersion"

    const val hiltTesting = "com.google.dagger:hilt-android-testing:${DaggerHilt.hiltVersion}"

    private const val assertKVersion = "0.26.1"
    const val assertK = "com.willowtreeapps.assertk:assertk:$assertKVersion"

    private const val mockitoVersion = "4.8.1"
    const val mockitoCore = "org.mockito:mockito-core:$mockitoVersion"
    const val mockitoInline = "org.mockito:mockito-inline:$mockitoVersion"
    const val mockitoAndroid = "org.mockito:mockito-android:$mockitoVersion"
    private const val mockitoKotlinVersion = "3.2.0"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion"
}

fun DependencyHandler.test() {
    testImplementation(Testing.junit4)
    testImplementation(Testing.junitAndroidExt)
    testImplementation(Testing.coroutinesVersion)
    testImplementation(Testing.assertK)
}

fun DependencyHandler.testWithJUnit5() {
    testImplementation(Testing.junit5)
    testRuntimeOnly(Testing.junit5Engine)
    testImplementation(Testing.junit5Params)
    testImplementation(Testing.junitAndroidExt)
    testImplementation(Testing.coroutinesVersion)
    testImplementation(Testing.assertK)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Testing.junit4)
    androidTestImplementation(Testing.junitAndroidExt)
    androidTestImplementation(Testing.coroutinesVersion)
    androidTestImplementation(Testing.espresso)
    androidTestImplementation(Testing.composeUiTestJunit4)
    androidTestImplementation(Testing.hiltTesting)
}

fun DependencyHandler.mockito() {
    testImplementation(Testing.mockitoCore)
    testImplementation(Testing.mockitoInline)
    androidTestImplementation(Testing.mockitoAndroid)
    androidTestImplementation(Testing.mockitoKotlin)
}