plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("dev.rikka.tools.refine")
}

val versionMajor = 0
val versionMinor = 5
val versionPatch = 1

val releaseStoreFile: String? by rootProject
val releaseStorePassword: String? by rootProject
val releaseKeyAlias: String? by rootProject
val releaseKeyPassword: String? by rootProject

android {
    namespace = "io.mesalabs.knoxpatch"
    compileSdk = 33

    defaultConfig {
        applicationId = "io.mesalabs.knoxpatch"
        minSdk = 28
        targetSdk = 33
        versionCode = versionMajor * 100000 + versionMinor * 1000 + versionPatch
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    }

    lint {
        disable += "AppCompatResource"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

configurations.all {
    exclude(group = "androidx.appcompat", module = "appcompat")
    exclude(group = "androidx.core", module = "core")
    exclude(group = "androidx.customview", module = "customview")
    exclude(group = "androidx.fragment", module = "fragment")
}

dependencies {
    // Sesl
    implementation("io.github.oneuiproject.sesl:appcompat:1.3.0")
    implementation("io.github.oneuiproject.sesl:material:1.4.0") {
        exclude(group = "io.github.oneuiproject.sesl", module = "viewpager2")
    }
    // AndroidX
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0") {
        exclude(group = "androidx.recyclerview", module = "recyclerview")
    }
    // Xposed
    compileOnly("de.robv.android.xposed:api:82")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    // Yuki
    implementation("com.highcapable.yukihookapi:api:1.1.11")
    ksp("com.highcapable.yukihookapi:ksp-xposed:1.1.11")
    // Rikka
    implementation("dev.rikka.tools.refine:runtime:4.3.0")

    compileOnly(project(":stub"))
}
