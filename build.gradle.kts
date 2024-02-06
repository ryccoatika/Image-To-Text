import com.android.build.gradle.BaseExtension
import com.diffplug.spotless.LineEnding
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

buildscript {
    dependencies {
        classpath(libs.kotlin.gradlePlugin)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp)
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.performance) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.tripletPlay) apply false
    alias(libs.plugins.ossLicenses) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory.get().asFile}/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(libs.versions.ktlint.get())
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            targetExclude("${layout.buildDirectory.get().asFile}/**/*.kts")
            ktlint(libs.versions.ktlint.get())
        }
        // https://github.com/diffplug/spotless/issues/1644
        lineEndings = LineEnding.PLATFORM_NATIVE // or any other except GIT_ATTRIBUTES
    }

    // Workaround for https://issuetracker.google.com/issues/268961156
    tasks.withType<com.android.build.gradle.internal.lint.AndroidLintTask> {
        val kspTestTask = tasks.findByName("kspTestKotlin")
        if (kspTestTask != null) {
            dependsOn(kspTestTask)
        }
    }
    tasks.withType<com.android.build.gradle.internal.lint.AndroidLintAnalysisTask> {
        val kspTestTask = tasks.findByName("kspTestKotlin")
        if (kspTestTask != null) {
            dependsOn(kspTestTask)
        }
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            allWarningsAsErrors.set(true)

            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )

            if (project.hasProperty("app.enableComposeCompilerReports")) {
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.layout.buildDirectory.get().asFile.absolutePath + "/compose_metrics",
                )
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.layout.buildDirectory.get().asFile.absolutePath + "/compose_metrics",
                )
            }
        }
    }

    // Configure Android projects
    pluginManager.withPlugin("com.android.application") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.library") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.test") {
        configureAndroidProject()
    }
}

fun Project.configureAndroidProject() {
    apply(plugin = libs.plugins.cacheFixPlugin.get().pluginId)

    extensions.configure<BaseExtension> {
        compileSdkVersion(libs.versions.compileSdk.get().toInt())

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
