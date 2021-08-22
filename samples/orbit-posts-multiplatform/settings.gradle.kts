pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Orbit_Posts_Multiplatform"
include(":androidApp")
include(":shared")
includeBuild("../../orbit-swift-gradle-plugin")
