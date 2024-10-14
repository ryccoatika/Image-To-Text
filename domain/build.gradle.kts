plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ryccoatika.imagetotext.domain"
}

dependencies {
    implementation(libs.hilt.dagger)
    ksp(libs.hilt.android.compiler)

    implementation(libs.coroutinesCore)
    implementation(libs.mlkit.visionCommon)
}
