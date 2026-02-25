import java.util.Properties
import java.io.File

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
            setUrl("https://nexus.kangyu.info/repository/maven-releases/")
            credentials {
                val localProps = loadLocalProperties()
                username = System.getenv("NEXUS_USERNAME")
                    ?: localProps.getProperty("nexusUsername", "")
                            ?: providers.gradleProperty("nexusUsername").getOrElse("")
                password = System.getenv("NEXUS_PASSWORD")
                    ?: localProps.getProperty("nexusPassword", "")
                            ?: providers.gradleProperty("nexusPassword").getOrElse("")
            }
        }
        maven {
            setUrl("https://nexus.kangyu.info/repository/maven-snapshots/")
            credentials {
                val localProps = loadLocalProperties()
                username = System.getenv("NEXUS_USERNAME")
                    ?: localProps.getProperty("nexusUsername", "")
                            ?: providers.gradleProperty("nexusUsername").getOrElse("")
                password = System.getenv("NEXUS_PASSWORD")
                    ?: localProps.getProperty("nexusPassword", "")
                            ?: providers.gradleProperty("nexusPassword").getOrElse("")
            }
        }
    }
}

rootProject.name = "WellnessDemo"
include(":app")


// load local.properties
fun loadLocalProperties(): Properties {
    val properties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}
