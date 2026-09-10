# Jarvis Android

A native Kotlin starter for voice-controlled calling. It recognizes commands such as `call John` and `call 555 123 4567`, looks up contacts, reads back the target, and requires confirmation before dialing.

## Build

Open this folder in Android Studio with JDK 17 and an Android SDK installed. Grant microphone, contacts, and phone permissions on the device.

The phone must be powered on. Screen-off operation depends on Android version, battery optimization, microphone policy, and the device manufacturer. A fully powered-off phone cannot run Jarvis.

## Hands-free mode

After installing, tap `Enable hands-free assistant` and choose Jarvis as the default digital assistant. This allows Android to invoke Jarvis from supported system surfaces and the lock screen. A third-party app cannot guarantee a custom `Hey Jarvis` wake phrase on every phone; true always-listening hotword detection requires device/OEM support or a dedicated hotword engine and may be restricted while the screen is locked.
"# jarvis-website" 
