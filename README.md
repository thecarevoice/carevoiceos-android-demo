---
aside: false
---

<SDKDocTabs
  platform="CareVoiceOS Android SDK Integration Guide"
  version="v3.1.0"
  :versions="[
    { value: 'v3.1.0', label: 'v3.1.0', isLatest: true, status: 'Latest' }
  ]"
>

<template #prerequisites>
Before you begin, ensure your development environment meets the following requirements:

*   **Android Studio:** Latest stable version recommended
*   **Gradle:** 8.4
*   **Android Gradle Plugin (AGP):** 8.4. or higher
*   **Kotlin Version:** 1.8.21 or higher
*   **Java Compatibility:** Java 17 or higher
*   **Minimum SDK Version:** API 27 (Android 8.1) or higher
*   **Compile SDK Version:** API 35 (Android 15)
*   **targetSdkVersion:** API 35 recommended
*   **CareVoice Credentials:** You must have valid credentials (`username`, `password`) provided by The CareVoice to access the private Maven repository
*   **Runtime Configuration:** You need a valid `baseUrl`, `tenantCode`, `accessToken`, and, if you want the SDK to refresh tokens before expiry, also provide `refreshToken` and `expiresIn`

</template>

<template #installation>

#### Configure Repositories (`settings.gradle.kts` or `settings.gradle`)

Add the CareVoice private Maven repository to your project's **root-level** `settings.gradle.kts` (or `settings.gradle`) file within the `dependencyResolutionManagement` block.

```kotlin
// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven {
            url = uri("https://nexus.kangyu.info/repository/maven-releases/")
            credentials {
                username = "Your_CareVoice_Username"
                password = "Your_CareVoice_Password"
            }
        }
    }
}

rootProject.name = "YourApplicationName"
include(":app")
```

**Explanation:**
*   This configuration tells Gradle where to find the SDK artifact.
*   Replace the placeholder credentials with the actual values provided by The CareVoice.
*   Avoid hardcoding credentials directly in version-controlled files. Prefer Gradle properties or environment variables.

#### Add SDK Dependency (App-Level `build.gradle.kts`)

In your **app-level** `build.gradle.kts` (or `build.gradle`) file, add the SDK dependency.

```kotlin
// app/build.gradle.kts

dependencies {
    implementation("com.carevoice.wellness:wellness:3.1.0")
}
```

If you consume a release build instead of a snapshot build, replace the version with the published release version provided by CareVoice.

#### Update Android Manifest

Ensure your `AndroidManifest.xml` includes the `INTERNET` permission, as the SDK needs network access.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application ...>
        <!-- ... -->
    </application>

</manifest>
```

The SDK AAR already contributes its own manifest entries for SDK Activities and Services. Your host app remains responsible for requesting any runtime permissions required by the user flow you expose.

</template>

<template #initialization>

### Initialization Model

The current Android SDK uses a **two-phase initialization flow**:

1. **Application initialization** at app startup
2. **User session initialization** after your authentication flow returns runtime auth values

Call `initApplication(...)` once in your `Application`. After your app has valid auth data, call `Wellness.init()` from a coroutine.

### Required Runtime Values

To initialize the signed-in SDK session, you need the following values:

| Parameter | Description | Source |
|-----------|-------------|--------|
| `baseUrl` | CareVoiceOS API host used by the SDK | Provided by CareVoice / environment config |
| `tenantCode` | Your organization's tenant identifier | Provided by CareVoice |
| `accessToken` | CareVoiceOS access token for API calls | Obtained from authentication flow |
| `refreshToken` | Token used by the SDK for token pre-refresh | Obtained from authentication flow |
| `expiresIn` | Remaining token lifetime in seconds | Obtained from authentication flow |

> `setExpiresIn(...)` expects **remaining seconds**, not a Unix timestamp.

### Step 1: Initialize In `Application`

Initialize the SDK once in your host application's `Application` class.

```kotlin
package com.yourcompany.yourapp

import android.app.Application
import com.carevoice.mindfulnesslibrary.Wellness
import java.util.Locale

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Wellness
            .setBaseUrl(BuildConfig.HOST)
            .setLocale(Locale.getDefault())
            .initApplication(this)

        Wellness.registerTokenExpiredCallback {
            sessionCoordinator.onWellnessSessionExpired()
        }

        Wellness.registerConsentGateCallback { event ->
            consentCoordinator.onWellnessConsentDismissed(event)
        }
    }
}
```

This step initializes application context, translations, Koin wiring, and shared SDK runtime dependencies.

If your app has a cached signed-in state during cold start, you may set the cached access token before `initApplication()`, but you should still perform the full user session initialization after the auth payload has been restored.

### Step 2: Initialize After Authentication

After your backend authentication succeeds, pass the runtime auth values to `Wellness` and then call `Wellness.init()` from a coroutine.

```kotlin
package com.yourcompany.yourapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carevoice.mindfulnesslibrary.Wellness
import java.util.Locale
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call setupWellnessSDK(...) after your auth flow returns valid values.
    }

    private fun setupWellnessSDK(
        baseUrl: String,
        tenantCode: String,
        accessToken: String,
        refreshToken: String,
        expiresInSeconds: Long,
    ) {
        lifecycleScope.launch {
            Wellness
                .setBaseUrl(baseUrl)
                .setTenantCode(tenantCode)
                .setToken(accessToken)
                .setRefreshToken(refreshToken)
                .setExpiresIn(expiresInSeconds)
                .setLocale(Locale.getDefault())

            Wellness.init()
            launchWellnessHub()
        }
    }

    private fun launchWellnessHub() {
        // Launch from one of the usage patterns shown below.
    }
}
```

### `tokenExpired` Callback Registration

Register the token expiration callback once, ideally in `Application`.

```kotlin
Wellness.registerTokenExpiredCallback {
    sessionCoordinator.onWellnessSessionExpired()
}
```

Current behavior:

*   If `refreshToken` and `expiresIn` are provided, the SDK attempts token pre-refresh before the access token expires
*   If a request still returns HTTP `401`, the SDK clears its cached runtime state and invokes the registered token-expired callback
*   The host app is responsible for deciding whether to refresh the host session, log out, or redirect the user

### SDK Consent Dismiss Callback Registration (`consentGateObserver`)

Register the SDK consent dismissal callback once, ideally in `Application`.

```kotlin
Wellness.registerConsentGateCallback { event ->
    when (event.feature) {
        Wellness.ConsentGateFeature.HUB -> {
            // User dismissed the SDK consent gate while entering Hub
        }
        Wellness.ConsentGateFeature.REWARD -> {
            // User dismissed the SDK consent gate while entering Reward
        }
    }
}
```

Current event shape:

```kotlin
Wellness.ConsentGateEvent(
    feature = Wellness.ConsentGateFeature.HUB, // or REWARD
    reason = Wellness.ConsentGateDismissReason.CLOSE_BUTTON,
)
```

Use this callback when your host app needs to close the current page, show fallback UI, or record analytics when the user explicitly dismisses the SDK agreement gate.

</template>

<template #usage>

Once the SDK has completed both initialization phases, you can use it in three common ways: standalone Activity, Compose embedding, or Android View embedding.

#### Option 1: Launch A Standalone SDK Activity

Use `WellnessTool.startHubViewActivity(...)` when you want the SDK to manage its own full-screen page.

```kotlin
package com.yourcompany.yourapp

import android.content.Context
import com.carevoice.mindfulnesslibrary.WellnessTool

fun openWellness(context: Context) {
    WellnessTool.startHubViewActivity(context)
}
```

#### Option 2: Embed The SDK In Compose

Use `WellnessMainScreen` when your host app is already Compose-based.

```kotlin
package com.yourcompany.yourapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.carevoice.mindfulnesslibrary.bridgeview.WellnessMainScreen

@Composable
fun WellnessRoute() {
    WellnessMainScreen(
        sdkConsentBottomHeight = 100.dp
    )
}
```

Use `sdkConsentBottomHeight` when your host app has a persistent bottom bar or bottom safe area that the SDK consent gate should avoid covering.

#### Option 3: Embed The SDK In The Android View System

Use `WellnessComposeView` when your host app is still view-based.

```xml
<com.carevoice.mindfulnesslibrary.bridgeview.WellnessComposeView
    android:id="@+id/wellnessView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

`WellnessComposeView` internally renders `WellnessMainScreen()` and can be hosted inside a normal `Activity` or `Fragment`.

#### Optional: Reserve Bottom Space For The SDK Consent Gate

If your host app has a persistent bottom area, you may also set the reserved bottom height explicitly:

```kotlin
import androidx.compose.ui.unit.dp
import com.carevoice.mindfulnesslibrary.Wellness

Wellness.setSDKConsentBottomHeight(56.dp)
```

</template>

<template #sample-app>

### Current Workspace Reference

The current workspace already contains a host app integration under `carevoiceos3-app/app`. It is the closest reference for how the SDK is used today.

#### Reference Integration Split

*   `app/src/main/java/com/carevoice/app/App.kt`
    Initializes `Wellness` in `Application` and registers `tokenExpired`
*   `app/src/main/java/com/carevoice/app/HomeSplashViewModel.kt`
    Sets `accessToken`, `refreshToken`, `tenantCode`, `baseUrl`, and calls `Wellness.init()`
*   `app/src/main/java/com/carevoice/app/MainActivity.kt`
    Sets `Wellness.setSDKConsentBottomHeight(100.dp)`
*   `app/src/main/java/com/carevoice/app/ui/login/WelComeRoute.kt`
    Launches `WellnessTool.startHubViewActivity(ctx)`

#### Reference `Application` Setup

```kotlin
private fun initSDK() {
    if (UserInfo.isLogin()) {
        Wellness.setToken(UserInfo.accessToken)
    }

    Wellness
        .setLocale(Locale.getDefault())
        .setBaseUrl(BuildConfig.HOST)
        .initApplication(this@App)

    Wellness.registerTokenExpiredCallback {
        UserInfo.logout()
    }
}
```

#### Reference User Session Setup

```kotlin
private suspend fun initWellnessSDK() {
    Wellness
        .setToken(UserInfo.accessToken)
        .setRefreshToken(UserInfo.refreshToken)
        .setExpiresIn(UserInfo.expiresIn)
        .setBaseUrl(BuildConfig.HOST)
        .setTenantCode(currentTenantCode())
        .setLocale(Locale.getDefault())

    Wellness.init()
}
```

</template>

</SDKDocTabs>

<div class="release-notes-section">

<h1> 📝 Recent Release Notes</h1>

### Version 3.1.0 <Badge type="tip" text="Latest" /> {#v3-1-0}

#### 🎉 Highlights
- Updated the Android integration guide to match the current `Wellness` / `WellnessTool` API
- Documented the two-phase initialization flow: `Application` bootstrap and post-auth session bootstrap
- Added official usage guidance for standalone Activity, Compose embedding, and `WellnessComposeView`

#### 🐛 Bug Fixes
- Restored SDK token pre-refresh support when `setRefreshToken(...)` and `setExpiresIn(...)` are provided together

</div>

<style scoped>
.release-notes-section {
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  padding: 2rem;
  background: var(--vp-c-bg-soft);
  margin-top: 2rem;
}

.release-notes-section h2 {
  margin-top: 0 !important;
}

.release-notes-section h1 {
  font-size: 1.5rem;
}
</style>
