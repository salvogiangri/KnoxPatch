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
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "io.mesalabs.knoxpatch"
        minSdk = 28
        targetSdk = 36
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
    implementation(libs.sesl.appcompat)
    implementation(libs.sesl.material)
    implementation(libs.androidx.window)
    compileOnly(libs.xposed.api)
    implementation(libs.yukihookapi.api) {
        exclude(group = "androidx.appcompat", module = "appcompat")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.preference", module = "preference-ktx")
        exclude(group = "com.google.android.material", module = "material")
    }
    ksp(libs.yukihookapi.ksp)
    implementation(libs.kavaref.core)
    implementation(libs.kavaref.extension)
    implementation(libs.lsposed.hiddenapibypass)
    implementation(libs.rikka.refine.runtime)
    compileOnly(projects.stub)
}
