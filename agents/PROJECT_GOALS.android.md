# PROJECT_GOALS — GameTrackr Android (Kotlin / Jetpack Compose)

> Read this together with `PROJECT_CONTEXT.md` (shared product + API contract).
> This file covers only the **Android-specific** stack, architecture, and milestones.

## Developer context

Frontend/mobile developer with solid experience in React Native, Expo, and TypeScript,
expanding into native development toward senior level. Already comfortable with the mobile
release lifecycle (CI/CD, store publishing). This app is the **Android half** of GameTrackr;
the iOS app is built in parallel against the same backend (see the iOS repo).

The backend (Laravel + Reverb) and the Vue web app are built by a collaborator. This repo
consumes that API — no business logic is invented here.

---

## Project goal

Build a native Android app in **Kotlin + Jetpack Compose** that consumes the GameTrackr API.
Full focus on **learning idiomatic Android** with good practices: Compose, Coroutines/Flow,
a clean MVVM (or MVI) architecture, secure token storage, and a typed networking layer.

---

## Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3 (kept neutral so the design matches the iOS app)
- **Architecture:** MVVM (or MVI) with a Repository layer; unidirectional data flow with `StateFlow`
- **DI:** Hilt
- **Networking:** Retrofit + OkHttp + `kotlinx.serialization` (or Moshi) — *or* Ktor Client if preferred
- **Async:** Coroutines + Flow
- **Secure storage:** EncryptedSharedPreferences or DataStore + Jetpack Security for tokens
- **Images:** Coil
- **Navigation:** Navigation-Compose
- **Realtime:** `pusher-websocket-java` (Reverb / Pusher protocol)
- **Testing:** JUnit + Turbine (Flow) + MockWebServer; Compose UI tests
- **Tooling:** ktlint/detekt, GitHub Actions CI

---

## Expected screens

See `PROJECT_CONTEXT.md` → *Feature scope by phase*. Auth screens are already designed
(Welcome, Sign in, Create account, Forgot/Reset password, Verify email, Success). Build the
**MVP slice** first (auth → library → profile), then layer discovery, friends, community,
messaging, and collection.

---

## Technical requirements

### Secure storage
- Access token (and refresh token, if used) in **EncryptedSharedPreferences / encrypted DataStore**.
- Logout clears all tokens.

### Auth interceptor
- OkHttp `Authenticator` / `Interceptor` that attaches the Bearer token.
- On `401`: attempt refresh (if the backend uses refresh tokens) with a **single-flight**
  guard so parallel requests don't trigger multiple refreshes; otherwise route to login.

### Networking
- Centralized API layer (Retrofit service interfaces per feature: `AuthApi`, `LibraryApi`, …).
- Typed error model mapped from the API's `{ message, errors }` shape and HTTP status codes.
- A `Result`/sealed-class wrapper for success/error/loading at the repository boundary.

### Architecture
- `View (Composable)` → `ViewModel (StateFlow)` → `Repository` → `ApiService` / `TokenStore`.
- ViewModels expose immutable UI state; one-off events via `Channel`/`SharedFlow`.
- No Android framework types leaking into ViewModels where avoidable.

### UI / UX
- Pure Compose, no XML layouts.
- Loading / empty / error states on every async screen.
- Pull-to-refresh on lists; swipe actions where it fits the design.
- Keep components neutral/cross-platform so the Android and iOS UIs stay visually aligned
  (no Material-only patterns the iOS app can't mirror).

---

## What to learn here

1. **Jetpack Compose** — state, recomposition, side-effects, Navigation-Compose
2. **Coroutines + Flow** — `StateFlow`, `combine`, `Turbine` testing
3. **Hilt** — dependency injection on Android
4. **Retrofit/OkHttp** — interceptors, the equivalent of axios interceptors for token refresh
5. **EncryptedSharedPreferences / DataStore** — secure token storage on Android
6. **MVVM/MVI** — unidirectional state on Android
7. **Pusher/Reverb on Android** — consuming Laravel Reverb channels
8. **Compose testing + MockWebServer** — testing networking and UI

---

## Milestones

| #  | Deliverable | Estimate |
| --- | --- | --- |
| 1 | Project setup: Compose, Hilt, modules/packages, `TokenStore` | a few hours |
| 2 | Networking: Retrofit + serialization + typed error model + auth interceptor | half a day |
| 3 | Auth screens wired to real API (login, register, token persistence) | a weekend |
| 4 | Token refresh with single-flight guard (if backend uses refresh) | + half a day |
| 5 | Library: list with status filter, add game, edit entry | + 1 day |
| 6 | Game search + detail → add to library | + 1 day |
| 7 | Profile + stats + sign out | + half a day |
| 8 | Discovery feeds (releases / upcoming / trending) | + 1 day |
| 9 | Friends + public profiles | + 1 day |
| 10 | Realtime messaging via Reverb + push notifications | + 2 days |
| 11 | Community + physical collection (image upload) | + 2 days |
| 12 | Polish: states, animations, app icon, CI | + 1 day |

---

## Progress log

### 2026-06-24 — Auth UI: Welcome + Sign-up (milestone 3, UI-first)

Auth screens are being built **UI-first** (no networking yet) to match the iOS app pixel-for-pixel.

- **Welcome** and **Register (sign-up)** screens implemented in pure Compose, mirroring the
  iOS `RegisterView` structure component-for-component.
- **Reusable auth components** in `core/ui/`:
  - `components/AuthScreenScaffold` — dark background + scroll + `safeDrawing` insets
  - `components/AuthTextField` — labeled field, focus/error border, placeholder, password
    reveal toggle (uses `material-icons-extended`)
  - `components/TitleWithSubtitle`, `components/PasswordStrengthMeter` (+ `PasswordStrength` enum)
  - `components/PrimaryButton`, `components/SecondaryButton`, plus `pressScale`, `glow`,
    `anim/staggeredAppear` modifiers for the shared motion language
- **Register feature** in `feature/auth/register/`:
  - `RegisterScreen` composes the sections; each section is its own file under
    `register/components/` (`BackButton`, `RegisterFormSection`, `TermsAcceptanceRow`,
    `RegisterBottomSection`, `SocialLoginSection`) — same split as iOS `Register/Components/`.
  - `RegisterFormState` is a plain Compose state holder (not an AndroidX `ViewModel` yet —
    `lifecycle-viewmodel-compose`/Hilt aren't wired). It mirrors the iOS `RegisterViewModel`
    validation: errors only after `submit()`, `@StringRes` error ids resolved in the UI.
    **Promote to a real `ViewModel` + Hilt when networking lands (milestones 2–3).**
- **Navigation** (`navigation/AppNavGraph`):
  - `navigateOnce()` guard ignores duplicate taps (only navigates while the source entry is
    `RESUMED`) — fixes the screen opening twice on fast double-tap.
  - White-flash-on-transition fixed by setting the activity `windowBackground` to the dark
    app background in `themes.xml` (parent was `Material.Light`).
- **Field naming:** the UI label is **"Name"** but it maps to `username` in
  `POST /auth/register` — keep both clients + the API aligned (see shared `CLAUDE.md`).
- **Dependency added:** `androidx.compose.material:material-icons-extended` (password eye icon).

> Heads-up for testing: the **Android emulator NAT can drop its default route**
> (`ip route` shows no `default via …` → `ERR_ADDRESS_UNREACHABLE` in Chrome / "Network is
> unreachable"). It's an emulator/host networking glitch, **not** app code — cold-boot/wipe
> the AVD (and disconnect any host VPN). The Terms/Privacy links themselves open correctly.

---

## Folder structure (suggested)

```
app/src/main/java/app/gametrackr/
├── GameTrackrApp.kt              # @HiltAndroidApp
├── core/
│   ├── network/
│   │   ├── ApiClient.kt          # OkHttp + Retrofit setup
│   │   ├── AuthInterceptor.kt
│   │   ├── ApiError.kt           # typed error model
│   │   └── Result.kt
│   ├── auth/
│   │   └── TokenStore.kt         # EncryptedSharedPreferences / DataStore
│   ├── realtime/
│   │   └── ReverbClient.kt
│   └── ui/                       # shared composables, theme/design tokens
├── feature/
│   ├── auth/                     # screens + viewmodels
│   ├── library/
│   ├── discovery/
│   ├── profile/
│   ├── friends/
│   ├── messaging/
│   ├── community/
│   └── collection/
├── data/
│   ├── repository/               # one repo per feature
│   └── remote/                   # Retrofit service interfaces
└── model/                        # domain models / DTOs
```

---

## How to use this context with an AI

Paste **`PROJECT_CONTEXT.md` + this file**, then add an instruction, e.g.:

> "Based on this context, set up the Retrofit `ApiClient` with an `AuthInterceptor` and a typed `ApiError` mapped from the API's 422 shape."

> "Based on this context, I'm on milestone 5. Implement `LibraryRepository`, `LibraryViewModel` (StateFlow), and the Compose list with a status filter."

> "Based on this context, review this ViewModel and point out what a senior Android dev would change."
