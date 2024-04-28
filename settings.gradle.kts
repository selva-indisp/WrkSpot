pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WrkSpot"
include(":app")
include(":designsystem")
include(":network")
include(":core")
include(":country")
