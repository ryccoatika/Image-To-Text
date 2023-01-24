plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    useBuildCache = true
}

android {
    namespace = "com.ryccoatika.imagetotext.domain"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.hilt.dagger)
    kapt(libs.hilt.android.compiler)
    implementation(libs.coroutines.core)

    implementation(libs.google.mlkit.textrecognition)
}