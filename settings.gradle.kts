rootProject.name = "image-to-text"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.google.android.gms.oss-licenses-plugin")) {
                useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// https://docs.gradle.org/7.6/userguide/configuration_cache.html#config_cache:stable
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
    ":ui",
    ":app",
    ":core",
    ":domain",
)
