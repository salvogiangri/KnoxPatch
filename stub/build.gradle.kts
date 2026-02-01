plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "io.mesalabs.stub"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    buildToolsVersion = "36.1.0"

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_21
    }

    enableKotlin = false
}

dependencies {
    implementation(libs.androidx.annotation)
    annotationProcessor(libs.rikka.refine.annotationprocessor)
    compileOnly(libs.rikka.refine.annotation)
}
