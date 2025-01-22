plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.rikka.refine)
    id("KnoxPatchPlugin")
}

val releaseStoreFile: String? by rootProject
val releaseStorePassword: String? by rootProject
val releaseKeyAlias: String? by rootProject
val releaseKeyPassword: String? by rootProject

android {
    namespace = "io.mesalabs.knoxpatch"
    compileSdk = 35
    buildToolsVersion = "35.0.1"

    defaultConfig {
        applicationId = "io.mesalabs.knoxpatch"
        minSdk = 28
        targetSdk = 35
        versionCode = Config.versionCode
        versionName = Config.versionName
    }

    lint {
        disable += "AppCompatResource"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    signingConfigs {
        create("release") {
            releaseStoreFile?.also {
                storeFile = rootProject.file(it)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        all {
            signingConfig =
                if (releaseStoreFile.isNullOrEmpty()) {
                    signingConfigs.getByName("debug")
                } else {
                    signingConfigs.getByName("release")
                }
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "sepCategory"

    productFlavors {
        create("sepBasic") {
            dimension = "sepCategory"
        }
        create("sepLite") {
            dimension = "sepCategory"
        }
    }

    sourceSets {
        named("sepBasic") {
            manifest.srcFile("config/AndroidManifest_SEPBasic.xml")
        }
        named("sepLite") {
            manifest.srcFile("config/AndroidManifest_SEPLite.xml")
        }
    }
}

kotlin {
    jvmToolchain(21)
}

configurations.all {
    exclude(group = "androidx.core", module = "core")
    exclude(group = "androidx.core", module = "core-ktx")
}

dependencies {
    // Sesl: https://github.com/tribalfs/sesl-androidx/tree/sesl7-androidx-main
    implementation(fileTree("src/main/libs") { include("*.aar") })

    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.constraintlayout) {
        exclude(group = "androidx.appcompat", module = "appcompat")
    }
    implementation(libs.androidx.customview.poolingcontainer)
    implementation(libs.androidx.emoji2.viewshelper)
    implementation(libs.androidx.window)
    implementation(libs.google.errorprone.annotations)
    compileOnly(libs.xposed.api)
    implementation(libs.yukihookapi.api) {
        exclude(group = "androidx.appcompat", module = "appcompat")
        exclude(group = "androidx.preference", module = "preference-ktx")
    }
    ksp(libs.yukihookapi.ksp)
    implementation(libs.lsposed.hiddenapibypass)
    implementation(libs.rikka.refine.runtime)
    compileOnly(projects.stub)
}
