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
    implementation("com.carevoice.mindfullness:mindfullness:0.8.0-SNAPSHOT")
    ....
}
```

# 3. 在Application 中初始化SDK
```
WellnessSDK.initSDK(application)
```


##4. 在 Activity 中初始化SDK相关参数
在 Activity 中初始化 SDK 并启动 HubViewActivity：
//Authorization 为token 

```
                WellnessSDK.setBaseUrl(NetUtils.getBaseUrl()).setToken("your token")
                    .setRefreshToken("your refresh token")
                    .setExpiresIn(" expiresIn ").setTenantCode("your tenant Code").init(ctx) {
                    WellnessTool.startHubViewActivity(ctx)
                }
```
