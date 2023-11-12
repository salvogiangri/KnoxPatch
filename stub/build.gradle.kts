plugins {
    id("com.android.library")
}

android {
    namespace = "io.mesalabs.stub"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        minSdk = 28
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    // HiddenApiRefinePlugin: https://github.com/RikkaApps/HiddenApiRefinePlugin
    annotationProcessor("dev.rikka.tools.refine:annotation-processor:4.4.0")
    compileOnly("dev.rikka.tools.refine:annotation:4.4.0")
    // AndroidX: https://developer.android.com/jetpack/androidx/versions
    implementation("androidx.annotation:annotation:1.7.0")
}
