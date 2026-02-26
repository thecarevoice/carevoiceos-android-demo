# CV Wellness SDK for Android

## Android SDK Usage Steps

### 1. Add the Target Maven Repository

Open your root `settings.gradle.kts` file and add the following repository configuration. Note that this now includes both `maven-releases` and `maven-snapshots` repositories.

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

// This helper function is needed in settings.gradle.kts to load local.properties
fun loadLocalProperties(): java.util.Properties {
    val properties = java.util.Properties()
    val localPropertiesFile = java.io.File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}
```

### 2. Add the dependency in your `app/build.gradle.kts`
```kotlin
dependencies {
    implementation("com.carevoice.wellness:wellness:3.0.4")
    implementation("com.carevoice.cvdesign:cvdesign:3.0.4")
    // ... other dependencies
}
```

### 3. Initialize the SDK in your Application class
Initialize the `Wellness` SDK in the `onCreate` method of your `Application` class.

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
Make sure to register your `Application` class in the `<application>` tag of your `AndroidManifest.xml`:

```xml
<application
    android:name=".MainApplication"
    ...>
</application>
```

### 4. Initialize SDK parameters
In your Activity or Composable, use the builder pattern to set the required parameters, then call the `init()` method.

```kotlin
import com.carevoice.mindfulnesslibrary.Wellness
import java.util.Locale

// ...

// Asynchronous call
Wellness.setToken("your access token")
    .setTenantCode("your tenant code")
    .setExpiresIn("your token expiration time".toLong())
    .setRefreshToken("your refresh token")
    .setBaseUrl("your base url")
    .setLocale(Locale.getDefault()) // Optional

Wellness.init() // Finally, call init()
```
For example, after a successful login process:

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
        .setBaseUrl(CareVoiceOS.getBaseUrl()) // Replace with your Base URL
        .setLocale(Locale.getDefault())
    Wellness.init()
}
```

### 5. Displaying the Wellness UI

The Wellness SDK provides two ways to display its user interface: launching it as a standalone `Activity` or embedding it as a `Composable` in your existing Jetpack Compose UI.

#### 5.1 Launching as a new Activity

You can launch the main screen of the Wellness SDK as a new activity. This is the simplest way to integrate the SDK's UI. Use `WellnessTool.startHubViewActivity()` and pass the current `Context`.

**Example:**
```kotlin
import com.carevoice.mindfulnesslibrary.WellnessTool
import androidx.compose.ui.platform.LocalContext

// ...

val context = LocalContext.current
Button(
    onClick = {
        // Starts the Wellness SDK's main activity
        WellnessTool.startHubViewActivity(context)
    }
) {
    Text("Enter Wellness SDK")
}
```

#### 5.2 Embedding as a Composable

If you want to integrate the Wellness SDK's UI seamlessly within your own Composable screens, you can use the `WellnessMainScreen` Composable.

**Example:**

This shows how to place the `WellnessMainScreen` inside a `Column`.

```kotlin
import com.carevoice.mindfulnesslibrary.bridgeview.WellnessMainScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// ...

@Composable
fun WellnessScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
    ) {
        // Embed the Wellness main screen as a Composable
        WellnessMainScreen()
    }
}
```

### 6. Configuration
1. The request address for login and registration is modified in the `getBaseUrl()` method in `NetUtils`.
2. The `BaseUrl` of the Wellness SDK is modified in the `getBaseUrl()` method in `CareVoiceOS`.
