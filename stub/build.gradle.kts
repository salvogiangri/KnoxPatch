plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "io.mesalabs.stub"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    annotationProcessor(libs.rikka.refine.annotationprocessor)
    compileOnly(libs.rikka.refine.annotation)
}
