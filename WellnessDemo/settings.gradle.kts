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
        maven(url="https://maven.lokalise.com")
        maven ( url ="https://jitpack.io" )
        maven {
            setUrl("https://nexus.kangyu.info/repository/maven-snapshots/")
            //上传服务器的账户密码
            credentials {
                username = "cvpartner"
                password = "wVSmP7GU8z"
            }
        }
    }
}

rootProject.name = "WellnessDemo"
include(":app")
 