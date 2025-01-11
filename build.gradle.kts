import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    afterEvaluate {
        extensions.findByType<KotlinProjectExtension>()?.apply {
            jvmToolchain(21)
        }
    }
}
