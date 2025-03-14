enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/tribalfs/sesl-androidx")
            credentials {
                username = extra.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = extra.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/tribalfs/sesl-material-components-android")
            credentials {
                username = extra.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
                password = extra.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
            }
        }
        maven(url = "https://jitpack.io")
        maven(url = "https://api.xposed.info")
    }
}

fun ExtraPropertiesExtension.findProperty(name: String): String? {
    return if (has(name)) {
        get(name) as String?
    } else {
        null
    }
}

rootProject.name = "KnoxPatch"
include(":app")
include(":stub")
