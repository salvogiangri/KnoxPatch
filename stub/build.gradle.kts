plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "io.mesalabs.stub"
    compileSdk = 35
    buildToolsVersion = "35.0.1"

    defaultConfig {
        minSdk = 28
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    annotationProcessor(libs.rikka.refine.annotationprocessor)
    compileOnly(libs.rikka.refine.annotation)
}
