import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class MainGradlePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        setProjectConfig(project)
        applyDependencies(project)
    }

    private fun applyDependencies(project: Project) {
        project.dependencies.coreKtx()
        project.dependencies.desugaring()
    }

    private fun applyPlugins(project: Project) {
        project.apply {
            //plugin("android-library")
            plugin("kotlin-android")
            plugin("kotlin-kapt")
            plugin("dagger.hilt.android.plugin")
        }
    }

    private fun setProjectConfig(project: Project) {
        val androidExtension = project.extensions.getByName("android")
        if (androidExtension is BaseExtension) {
            androidExtension.apply {
                compileSdkVersion(ProjectConfig.compileSdk)

                defaultConfig {
                    minSdk = ProjectConfig.minSdk
                    targetSdk = ProjectConfig.targetSdk
                    versionCode = ProjectConfig.versionCode
                    versionName = ProjectConfig.versionName
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                val proguardFile = "proguard-rules.pro"
                when(this) {
                    is LibraryExtension -> defaultConfig {
                        consumerProguardFiles(proguardFile)
                    }
                    is AppExtension -> buildTypes {
                        getByName("release") {
                            isMinifyEnabled = true
                            isShrinkResources = true
                            debuggable(false)
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                proguardFile
                            )
                        }
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_18
                    targetCompatibility = JavaVersion.VERSION_18
                    isCoreLibraryDesugaringEnabled = true
                }
                project.tasks.withType(KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        jvmTarget = "18"
                    }
                }
            }
        }
    }

    private fun Project.androidLibrary(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }

    private fun Project.androidApp(): AppExtension {
        return extensions.getByType(AppExtension::class.java)
    }
}