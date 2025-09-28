pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url ="https://jitpack.io" )
        maven {
            setUrl("https://nexus.kangyu.info/repository/maven-snapshots/")
            credentials {
                username = "replace_me"
                password = "replace_me"
            }
        }
    }
}

rootProject.name = "WellnessDemo"
include(":app")
 