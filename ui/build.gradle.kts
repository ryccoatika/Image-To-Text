plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ryccoatika.imagetotext.ui"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.accompanist.permission)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)

    implementation(libs.lifecycle.viewmodel)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil)

    implementation(libs.google.mlkit.textrecognition)

    implementation(libs.google.play.review)
}
