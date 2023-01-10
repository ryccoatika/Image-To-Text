plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.ryccoatika.imagetotext.ui"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.2")

    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")

    implementation("com.google.accompanist:accompanist-pager:0.28.0")

    implementation("com.himanshoe:pluck:1.0.0-RC2")
}