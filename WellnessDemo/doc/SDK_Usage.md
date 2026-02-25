# CV Wellness SDK Android

## Android SDK 使用步骤

### 1. 添加目标 Maven 仓库

打开您的根 `settings.gradle.kts` 文件并添加以下仓库配置。请注意，现在同时包含了 `maven-releases` 和 `maven-snapshots` 仓库。

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url = "https://jitpack.io" )
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

// 需要在 settings.gradle.kts 文件中添加这个辅助函数来加载 local.properties
fun loadLocalProperties(): Properties {
    val properties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}
```

### 2. 在 `app/build.gradle.kts` 中添加依赖

```kotlin
dependencies {
    implementation("com.carevoice.wellness:wellness:3.0.4")
    implementation("com.carevoice.cvdesign:cvdesign:3.0.4")
    // ... other dependencies
}
```

### 3. 在 Application 类中初始化 SDK

在您的 `Application` 类的 `onCreate` 方法中初始化 `Wellness` SDK。

```kotlin
import android.app.Application
import com.carevoice.mindfulnesslibrary.Wellness

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Wellness.initSDK(this)
    }
}
```
确保在 `AndroidManifest.xml` 的 `<application>` 标签中注册您的 `Application` 类：
```xml
<application
    android:name=".MainApplication"
    ...>
</application>
```

### 4. 初始化 SDK 参数并启动功能

在您的 Activity 或 Composable 中，使用链式调用来设置必要的参数，然后调用 `init()` 方法。

```kotlin
import com.carevoice.mindfulnesslibrary.Wellness
import java.util.Locale

// ...

// 异步调用
Wellness.setToken("your access token")
    .setTenantCode("your tenant code")
    .setExpiresIn("your token expiration time".toLong())
    .setRefreshToken("your refresh token")
    .setBaseUrl("your base url")
    .setLocale(Locale.getDefault()) // 可选

Wellness.init() // 最后调用 init()
```
例如，在您的登录流程成功后：

```kotlin
private suspend fun initWellnessSDK(
    token: String,
    expiresIn: Long,
    refreshToken: String,
    tenantCode: String
) {
    Wellness.setToken(token)
        .setTenantCode(tenantCode)
        .setExpiresIn(expiresIn)
        .setRefreshToken(refreshToken)
        .setBaseUrl(CareVoiceOS.getBaseUrl()) // 替换为您的Base URL
        .setLocale(Locale.getDefault())
    Wellness.init()
}
```
