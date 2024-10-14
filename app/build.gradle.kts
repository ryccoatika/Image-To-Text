plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)

    alias(libs.plugins.hilt)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.tripletPlay)
    alias(libs.plugins.ossLicenses)
}

hilt {
    enableAggregatingTask = true
}

val appVersionCode: Int = project.properties["VERSION_CODE"] as? Int? ?: 1
println("APK version code: $appVersionCode")

val isKeystoreReleaseExists = rootProject.file("release/release.jks").exists()

android {
    namespace = "com.ryccoatika.imagetotext"
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            if (isKeystoreReleaseExists) {
                storeFile = rootProject.file("release/release.jks")
                storePassword = project.properties["RELEASE_KEYSTORE_PWD"] as? String
                keyAlias = "imagetotext"
                keyPassword = project.properties["RELEASE_KEY_PWD"] as? String
            }
        }
    }
    defaultConfig {
        applicationId = "com.ryccoatika.imagetotext"

        versionCode = appVersionCode
        versionName = "2.0.1"

        testInstrumentationRunner = "androidx.intro_1.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions += "mode"
    productFlavors {
        create("qa") {
            signingConfig = signingConfigs.getByName("debug")

            dimension = "mode"
            versionNameSuffix = "-qa"
        }

        create("standard") {
            signingConfig = if (isKeystoreReleaseExists) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }

            dimension = "mode"
        }
    }

    lint {
        checkReleaseBuilds = false
        ignoreTestSources = true
        abortOnError = true
        checkDependencies = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
}

// Disable variant standard debug
androidComponents {
    beforeVariants { variantBuilder ->
        val isQa = variantBuilder.productFlavors.any { it.first.contains("qa") || it.second.contains("qa") }
        val isDebug = variantBuilder.buildType == "debug"
        if (!isQa && isDebug) {
            variantBuilder.enable = false
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.ui)

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.composeActivity)
    implementation(libs.compose.material)
    implementation(libs.androidx.composeNavigation)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.androidx.splashscreen)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.core)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    debugImplementation(libs.leakcanary)
}
