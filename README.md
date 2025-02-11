# CV Wellness SDK Android

# Android SDK 使用步骤

## 1. 添加目标 Maven 仓库

1. 打开 Android Studio 项目或工作空间。

根目录 settings.gradle.tks
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.lokalise.com")
        maven {
            setUrl("https://nexus.kangyu.info/repository/maven-snapshots/")
            //maven服务器账号密码
            credentials {
                username = "Your username"
                password = "Your password"
            }
        }
    }
}
```

## 2.在项目build.gradle.kts中(app/build.gradle.kts) 添加
```
dependencies {
    implementation("com.carevoice.mindfullness:mindfullness:0.5.0-SNAPSHOT")
    ....
}
```



## 3. 在 Activity 中初始化 SDK
在 Activity 中初始化 SDK 并启动 HubViewActivity：
//Authorization 为token 

```
val header = hashMapOf<String, String>().apply {
    put("Locale", "US")
    put("Time-Zone", TimeZone.getDefault().id)
    put("Agent", "Android")
    put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ijc5Mzg3MjNmNiJ9.eyJhdWQiOiI0YWEyMmRmYi1lNmQ3LTRiODQtOWYyNy1mNGYxMjljNmEyZmYiLCJleHAiOjE3MzgwNTYyMjksImlhdCI6MTczNzk2OTgyOSwiaXNzIjoiYWNtZS5jb20iLCJzdWIiOiI4NWZlYjI4Ny0xZDZhLTRkNDYtYjY1Ny01ZDQ0NGZiMTNkNzQiLCJqdGkiOiI3ZTNkNGM3Yi0wYWU2LTQwNzktOTQ0YS1mNGMxYzFmNTJmZDIiLCJhdXRoZW50aWNhdGlvblR5cGUiOiJQQVNTV09SRCIsImVtYWlsIjoiYXV0bzUwMDY5QHFxLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhcHBsaWNhdGlvbklkIjoiNGFhMjJkZmItZTZkNy00Yjg0LTlmMjctZjRmMTI5YzZhMmZmIiwicm9sZXMiOltdLCJhdXRoX3RpbWUiOjE3Mzc5Njk4MjksInRpZCI6IjZmYmI0YmUxLWVjZjgtNGNmMC05YWUyLTdkYjg4NDI2MjA4YSJ9.FJNEjtJNSFP6hJKAP830Q5mz1nJ3FlNm2rVv3PQlYKk")
    put("Version", "0.5.0")
    put("Accept-Encoding", "gzip")
    put("Lang", "en")
}

//协程
lifecycleScope.launch {
    MindfulnessSDK.init(this@MainActivity, "https://p2-stag.kangyu.info/", header)
}
//启动对应Activity
MindfulnessConfig.startHubViewActivity(this@MainActivity)
```
