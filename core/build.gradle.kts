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

    implementation(libs.coroutinesCore)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.mlkit.textrecognition)
    implementation(libs.mlkit.textrecognition.chinese)
    implementation(libs.mlkit.textrecognition.devanagari)
    implementation(libs.mlkit.textrecognition.japanese)
    implementation(libs.mlkit.textrecognition.korean)
    implementation(libs.gson)
}
