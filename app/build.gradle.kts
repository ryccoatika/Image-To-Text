plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ryccoatika.imagetotext"
        minSdk = 23
        targetSdk = 33
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.intro_1.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".dev"
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":ui"))

    implementation("androidx.activity:activity-compose:1.6.1")

    debugImplementation("androidx.compose.ui:ui-tooling:1.3.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.2")

    implementation("androidx.compose.material:material:1.3.1")

    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")

    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-analytics-ktx:21.2.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}
