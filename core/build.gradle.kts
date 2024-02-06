plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ryccoatika.imagetotext.core"
}

dependencies {
    implementation(projects.domain)
    implementation(libs.room)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.coroutines.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.datastore.preferences)

    implementation(libs.google.mlkit.textrecognition)
    implementation(libs.google.mlkit.textrecognition.chinese)
    implementation(libs.google.mlkit.textrecognition.devanagari)
    implementation(libs.google.mlkit.textrecognition.japanese)
    implementation(libs.google.mlkit.textrecognition.korean)
    implementation(libs.gson)
}
