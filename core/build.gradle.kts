plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.room:room-runtime:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:18.0.2")
}