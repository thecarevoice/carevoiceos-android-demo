# CV Wellness SDK Android

# Android SDK Usage Steps

## 1. Add the Target Maven Repository

1. Open your Android Studio project or workspace.

In the root `settings.gradle.kts`:
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://nexus.kangyu.info/repository/maven-snapshots/")
            //maven server username and password
            credentials {
                username = "Your username"
                password = "Your password"
            }
        }
    }
}
```

## 2. Add the dependency in your project's `build.gradle.kts` (app/build.gradle.kts)
```
dependencies {
    implementation("com.carevoice.mindfullness:mindfullness:0.11.1-SNAPSHOT")
    ....
}
```

# 3. Initialize the SDK in your Application class
```
WellnessSDK.initSDK(application)
```


## 4. Initialize SDK parameters in your Activity
Initialize the SDK in your Activity and start `HubViewActivity`:
// `Authorization` is the token

```
                WellnessSDK.setBaseUrl(NetUtils.getBaseUrl()).setToken("your token")
                    .setRefreshToken("your refresh token")
                    .setExpiresIn(" expiresIn ").setTenantCode("your tenant Code").init(ctx) {
                    WellnessTool.startHubViewActivity(ctx)
                }
```
