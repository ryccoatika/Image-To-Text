plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

kapt {
    useBuildCache = true
}

android {
    namespace = "com.ryccoatika.imagetotext.core"
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
    implementation(project(":domain"))
    implementation(libs.room)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    implementation(libs.coroutines.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.datastore.preferences)

    implementation(libs.google.mlkit.textrecognition)
    implementation(libs.google.mlkit.textrecognition.chinese)
    implementation(libs.google.mlkit.textrecognition.devanagari)
    implementation(libs.google.mlkit.textrecognition.japanese)
    implementation(libs.google.mlkit.textrecognition.korean)
    implementation(libs.gson)
}