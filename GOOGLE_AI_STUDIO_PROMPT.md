# Google AI Studio Prompt: Jarvis Android App

Create a complete native Android application named Jarvis using Kotlin, Jetpack Compose, and Android APIs.

## Core behavior

Jarvis is a voice-controlled personal assistant. The user should be able to say commands such as:

- "Call John"
- "Call my mother"
- "Dial 555 123 4567"
- "Open YouTube"
- "Go home"
- "Go back"
- "Turn on the flashlight"
- "Set volume to 50 percent"
- "Play music"
- "Send a message to John" (always require confirmation before sending)

The phone must never place a call, send a message, or perform a destructive action without showing or speaking the detected target and receiving confirmation.

## Hands-free behavior

Implement Android assistant integration using VoiceInteractionService and VoiceInteractionSessionService. Add an onboarding screen that asks the user to:

1. Grant microphone permission.
2. Grant contacts permission.
3. Grant phone-call permission.
4. Grant notification permission where required.
5. Choose Jarvis as the default digital assistant through RoleManager.
6. Disable battery optimization for Jarvis where the device permits it.

Support invocation from the lock screen and supported assistant gestures. Explain clearly that a fully powered-off phone cannot run software and that custom "Hey Jarvis" hotword detection depends on Android device/OEM support. Do not claim that every phone supports third-party hotwords.

## Calling

Use ContactsContract to search contacts by spoken name. Support direct phone numbers. Normalize spoken numbers safely. Before dialing, display and speak: "I found John, 555-123-4567. Should I call?" Use ACTION_CALL only after explicit confirmation and fall back to ACTION_DIAL if CALL_PHONE permission is unavailable.

## Voice recognition

Use SpeechRecognizer or a maintained on-device speech recognition engine. Handle recognition errors, retries, silence, unavailable recognition services, lifecycle cleanup, microphone permission denial, and screen-lock state. Do not keep multiple recognizers alive. Keep the foreground-service notification visible whenever microphone capture is active.

## Accessibility and phone control

If implementing AccessibilityService for navigation, tapping, scrolling, or typing, make it an optional separately enabled feature. Explain the sensitive permissions and do not conceal the service. Respect Android security boundaries and do not bypass lock screens, app security, banking protections, or user consent.

## UI

Create a clean Jarvis dashboard with:

- Current listening/idle state
- Last recognized command
- Confirmation panel for calls and messages
- Permission and assistant setup status
- Start/stop hands-free mode
- Activity log that stays on-device
- Clear error messages

### Visual interface specification

Use a polished, futuristic but practical interface. The home screen should have a dark charcoal background, cyan accent lighting, and high-contrast white text. Avoid excessive neon, gradients, decorative cards, or fake hologram effects.

Layout from top to bottom:

1. A compact top bar with the title `JARVIS`, connection status, and a settings icon.
2. A large central circular microphone control with a microphone icon. Use distinct idle, listening, processing, and error states. The listening state may use a restrained pulsing ring.
3. A short status label such as `Ready`, `Listening`, `Processing`, or `Needs confirmation`.
4. A transcript area showing the last recognized command.
5. A confirmation panel that appears only when a call or message needs approval. Show the contact name, number, action, and separate `Confirm` and `Cancel` icon buttons.
6. A row of compact quick actions for Call, Message, Navigate, Media, and Flashlight.
7. A setup section showing microphone, contacts, phone, default assistant, and battery-optimization status with clear action buttons for incomplete items.
8. A small on-device activity log with timestamps and privacy-safe command summaries.

Use Material 3 components, accessible touch targets, screen-reader labels, responsive layouts for phones, and both light and dark themes. Keep all primary actions visible without opening nested menus. Use familiar icons from the Material icon library instead of manually drawn SVG icons. Do not use a marketing hero page.

Use accessible large controls, dark and light themes, and responsive Compose layouts. Never collect or upload contacts, call history, microphone recordings, or messages by default.

## Project requirements

Generate a complete Android Studio project with:

- Gradle Kotlin DSL
- Kotlin and Jetpack Compose
- minSdk 26 and current stable compileSdk
- AndroidManifest.xml with only required permissions
- Runtime permission handling
- VoiceInteractionService and session service
- Foreground microphone service with notification channel
- Unit tests for command parsing and phone-number normalization
- Instrumentation tests for the main flow where practical
- README.md with Android Studio setup, emulator/device instructions, permissions, default-assistant setup, limitations, and build commands
- No hardcoded secrets, network APIs, or analytics

Build a working first version rather than a marketing landing page. If a requested capability is blocked by Android, implement the closest permitted behavior and explain the limitation in the README and onboarding UI.
