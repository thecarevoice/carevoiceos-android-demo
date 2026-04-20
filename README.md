### Watch the Integration Demo

To help you get started, we have created a step-by-step demo video that walks you through the entire Android integration process.

The integration process is designed to be straightforward, allowing you to focus on building your Android app while we handle the SDK integration.

Watch the video below and follow along to complete your Android SDK integration in under an hour:

<video width="800" controls>
  <source src="https://carevoiceos-public.s3.us-west-2.amazonaws.com/CareVoiceOS-Android-demo.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

### Running Instructions

This Android application will simulate the MyClient application and integrate the CareVoiceOS SDK.

**Clone the Android Demo Repository**
   
   Download the Android Demo repository: `https://github.com/thecarevoice/carevoiceos-android-demo`
   
   Execute the following commands in your terminal:
   ```bash
   git clone https://github.com/thecarevoice/carevoiceos-android-demo
   cd carevoiceos-android-demo
   ```

**Open Project in Android Studio**
   
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `carevoiceos-android-demo/WellnessDemo` directory and select it
   - Wait for Gradle sync to complete

**Configure Maven Repository Access**
   
   **Note**: If you need to log in to access CareVoice Maven repository during the setup, please refer to `nexus-credential.md` for the necessary credentials.
   
   Ensure your `settings.gradle.kts` (project root) includes the CareVoice Maven repository:
   ```kotlin
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
   ```

**Update Backend API Configuration**
   
   - Navigate to the Android source code and locate the network configuration file `NetUtils.kt`
   - The demo app should be configured to connect to your backend simulation service at `http://localhost:3005`
   - If running on a physical device, you may need to use your computer's IP address instead of `localhost`
   - For emulator: Use `http://10.0.2.2:3005` to access the host machine's localhost
