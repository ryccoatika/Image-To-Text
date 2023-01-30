plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

hilt {
    enableAggregatingTask = true
}

val appVersionCode: Int = project.properties["VERSION_CODE"] as? Int? ?: 10
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
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ryccoatika.imagetotext"
        minSdk = 23
        targetSdk = 33
        versionCode = appVersionCode
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.intro_1.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
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
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":ui"))

    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.activity.compose)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.compose.material)

    implementation(libs.splashscreen)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.google.firebase.core)
    implementation(libs.google.firebase.analytics)

    debugImplementation(libs.leakcanary)
}
