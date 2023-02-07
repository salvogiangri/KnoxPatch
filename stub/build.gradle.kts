plugins {
    id("com.android.library")
}

android {
    namespace = "io.mesalabs.stub"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
        targetSdk = 33
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    annotationProcessor("dev.rikka.tools.refine:annotation-processor:4.1.0")
    compileOnly("dev.rikka.tools.refine:annotation:4.1.0")
    implementation("androidx.annotation:annotation:1.5.0")
}
