plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("dev.rikka.tools.refine")
    id("KnoxPatchPlugin")
}

val releaseStoreFile: String? by rootProject
val releaseStorePassword: String? by rootProject
val releaseKeyAlias: String? by rootProject
val releaseKeyPassword: String? by rootProject

android {
    namespace = "io.mesalabs.knoxpatch"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "io.mesalabs.knoxpatch"
        minSdk = 28
        targetSdk = 34
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

configurations.all {
    exclude(group = "androidx.appcompat", module = "appcompat")
    exclude(group = "androidx.core", module = "core")
    exclude(group = "androidx.fragment", module = "fragment")
    exclude(group = "androidx.recyclerview", module = "recyclerview")
}

dependencies {
    // Sesl: https://github.com/OneUIProject/oneui-core/tree/sesl4
    implementation("io.github.oneuiproject.sesl:appcompat:1.4.0")
    implementation("io.github.oneuiproject.sesl:material:1.5.0") {
        exclude(group = "io.github.oneuiproject.sesl", module = "viewpager2")
    }
    // AndroidX: https://developer.android.com/jetpack/androidx/versions
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // Xposed: https://github.com/LSPosed
    compileOnly("de.robv.android.xposed:api:82")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    // Yuki: https://github.com/fankes/YukiHookAPI
    implementation("com.highcapable.yukihookapi:api:1.2.1") {
        exclude(group = "androidx.preference", module = "preference")
    }
    ksp("com.highcapable.yukihookapi:ksp-xposed:1.2.1")
    // HiddenApiRefinePlugin: https://github.com/RikkaApps/HiddenApiRefinePlugin
    implementation("dev.rikka.tools.refine:runtime:4.4.0")

    compileOnly(project(":stub"))
}
