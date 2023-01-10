plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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
    implementation("com.google.dagger:dagger:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}