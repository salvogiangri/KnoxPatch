plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("KnoxPatchPlugin") {
            id = "KnoxPatchPlugin"
            implementationClass = "KnoxPatchPlugin"
        }
    }
}
