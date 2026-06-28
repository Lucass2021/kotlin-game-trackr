# PROJECT_GOALS — GameTrackr Android (Kotlin / Jetpack Compose)

> Read this together with `CLAUDE.md` (shared product + API contract).
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
- **Architecture:** MVVM with a Repository layer; unidirectional data flow with `StateFlow`
- **DI:** **Koin** — *was Hilt. Hilt's Gradle plugin is incompatible with this project's **AGP 9.0.0** (AGP 9 removed the `BaseExtension` API the plugin relies on, and no published Hilt supports it yet). Koin needs no Gradle plugin/KSP, so it works on AGP 9. See the 2026-06-27 progress log.*
- **Networking:** Retrofit + OkHttp + `kotlinx.serialization` (official `com.squareup.retrofit2:converter-kotlinx-serialization`)
- **Async:** Coroutines + Flow
- **Secure storage:** **DataStore (Preferences)** for tokens — *was "EncryptedSharedPreferences". The Jetpack Security Crypto lib (EncryptedSharedPreferences) was deprecated in 2024, so DataStore is the current path. Trade-off vs iOS: DataStore is **not encrypted at rest** (iOS uses the Keychain).*
- **Images:** Coil
- **Navigation:** Navigation-Compose
- **Realtime:** `pusher-websocket-java` (Reverb / Pusher protocol)
- **Testing:** JUnit + Turbine (Flow) + MockWebServer; Compose UI tests
- **Tooling:** ktlint/detekt, GitHub Actions CI

---

## Expected screens

See `CLAUDE.md` → *Feature scope by phase*. Auth screens are already designed
(Welcome, Sign in, Create account, Forgot/Reset password, Verify email, Success). Build the
**MVP slice** first (auth → library → profile), then layer discovery, friends, community,
messaging, and collection.

---

## Technical requirements

### Secure storage
- Access token in **DataStore (Preferences)**; logout clears it. (EncryptedSharedPreferences was
  the original plan, but the Jetpack Security Crypto lib is deprecated — see Stack note.)

### Auth interceptor
- OkHttp `Interceptor` attaches the Bearer token; OkHttp `Authenticator` handles `401`.
- **Verified backend behaviour:** auth is **JWT (tymon/jwt-auth), not Sanctum**, using
  **single-token rotation** — on `401`, call `POST /auth/refresh` with the *current* token in
  the header (no body); it returns a new token and blacklists the old one. There is **no separate
  refresh token**. The `Authenticator` does: retry-once + concurrent-refresh **dedup**
  (single-flight) + **network-failure ≠ auth-failure** (no logout when offline; only logout when
  refresh genuinely fails / token is dead).

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
3. **Koin** — dependency injection on Android (chosen over Hilt because of AGP 9 — see Stack)
4. **Retrofit/OkHttp** — interceptors, the equivalent of axios interceptors for token refresh
5. **DataStore** — token storage on Android (Flow-based; EncryptedSharedPreferences is deprecated)
6. **MVVM/MVI** — unidirectional state on Android
7. **Pusher/Reverb on Android** — consuming Laravel Reverb channels
8. **Compose testing + MockWebServer** — testing networking and UI

---

## Milestones

| #  | Deliverable | Estimate |
| --- | --- | --- |
| 1 | Project setup: Compose, Koin, modules/packages, `TokenStore` | a few hours |
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

### 2026-06-28 — Password-reset flow + reusable Success screen (milestone 3)

Ported the iOS-built password-reset flow **1:1** to Android. The porting spec lives in the
**sibling iOS repo**: `Projetos/GameTrackr/agents/password-reset-flow.md` (the iOS feature is
built first, then mirrored here). Flow:
`Login → Forgot → Verify (OTP) → Reset → Success → (pop) Login`, plus Register → Success → Home.

**Key decisions / gotchas**
- **Reset endpoints are mocked client-side.** The backend has no `password/forgot|reset` yet, so
  `forgotPassword` / `verifyResetCode` / `resetPassword` live in `AuthRepositoryImpl` with an
  artificial delay (any 6-digit code accepted, fake UUID reset token) — a one-line swap to the
  real API later. Marked with `// #TODO`.
- **Register defers authentication.** `register()` saves the token but **does not** flip the
  session to authenticated (it stashes `pendingUser`); otherwise the app jumps to Home and the
  Success screen is never seen. `SuccessScreen`'s `onPrimary` (button **or** 5s auto-redirect)
  calls `AuthViewModel.completeRegistration()`, which activates the session.
- **Pop-to-login after the flow** is driven by the single `NavController`:
  `popBackStack(Routes.LOGIN, inclusive = false)` from the reset Success — not a flag owned by a
  covered screen (a covered composable doesn't recompose). Reset/Verify VMs take a runtime arg
  (`email` / `resetToken`) via Koin `parametersOf`.
- **OTP error is inline, not a toast.** Code-validation errors render as red text **below** the
  OTP boxes (gated on first submit); the boxes themselves **never** turn red. The toast is
  reserved for API failures — matches the iOS `VerifyResetCodeView`.

**AuthScreenScaffold rework (fixes keyboard covering inputs on every auth screen)**
- Replaced the old `scrollable` + `safeDrawing` scaffold with the iOS model: `BoxWithConstraints`
  + `verticalScroll` + `heightIn(min = viewportHeight)` so content **centers when it fits and
  scrolls when the keyboard opens**, plus `imePadding()` + `WindowInsets.systemBars`.
- New slots: `onBack` (fixed top), `bottomBar` (pinned above the keyboard), and
  `contentArrangement` (e.g. `Center`). Applied to **all 5** auth screens (Login, Register,
  Forgot, Verify, Reset). Weighted `Spacer`s were removed (they can't coexist with a scroll).

**What shipped**
- Shared components: `core/ui/components/OtpField` (hidden numeric field + 6 boxes, auto-focus,
  auto-submit on the 6th digit) and `core/ui/components/SuccessScreen` (cyan check badge,
  optional "Account status" card, 5s auto-redirect countdown cancelled on dispose).
- Three features, each split into a `components/` folder like iOS:
  `feature/auth/forgotpassword`, `feature/auth/verifyresetcode`, `feature/auth/resetpassword`
  (Screen + ViewModel + UiState + `components/*FormSection`/`*BottomSection`).
- Login "Forgot my password" now navigates; the Verify screen has a 30s resend countdown.

### 2026-06-27 — Auth networking slice + launch flow (milestones 2–4)

Wired the auth UI to the real API and added the launch/refresh flow. Ported from a (now
deleted) iOS handoff doc; several decisions **diverge** from this file's original Stack —
recorded here so docs and code don't conflict.

**Key decisions / divergences**
- **DI = Koin, not Hilt.** This project runs **AGP 9.0.0 + Gradle 9.2.1** (note the new
  `compileSdk { release(36) { minorApiLevel = 1 } }` DSL). The latest published Hilt (2.56.2)
  fails at apply with *"Android BaseExtension not found"* — AGP 9 removed that API and no Hilt
  release supports it yet. Koin has no Gradle plugin/KSP, so it works today. Wiring lives in
  `di/AppModule.kt`, started in `GameTrackrApp.onCreate()`; ViewModels via `koinViewModel()`.
- **JSON = `kotlinx.serialization`** with the **official Square** converter
  (`com.squareup.retrofit2:converter-kotlinx-serialization`). (The JakeWharton 1.0.0 converter
  lacks `asConverterFactory`.)
- **Token storage = DataStore (Preferences)**, not EncryptedSharedPreferences (deprecated).
- **Backend auth is JWT (tymon/jwt-auth), not Sanctum** — single-token rotation on `401`. See the
  updated *Auth interceptor* section. This contradicts the shared `CLAUDE.md` "proposed" contract.
- **API base URL via a `config/` folder** mirroring iOS `Config/`: committed
  `config/debug.properties` + `config/release.properties`, gitignored `config/local.properties`
  (per-machine override, e.g. a physical device on the LAN IP), and `config/local.properties.example`.
  Gradle reads them into `BuildConfig.API_BASE_URL`. Emulator → `http://10.0.2.2:8000/api`
  (the host alias; `localhost` is the emulator itself). **Not** the Android Studio `local.properties`.

**What shipped**
- Network layer: `AuthApi` + `RefreshApi` (separate OkHttp client, no authenticator → avoids
  refresh recursion), `AuthInterceptor` (Bearer), `TokenAuthenticator` (refresh + retry-once +
  dedup + network≠auth), typed `ApiError` sealed class + 422 error mapper.
- `TokenStore` (DataStore), `SessionManager` (`StateFlow<AuthStatus>`), `AuthRepository`
  returning `Result<User>`. `LoginViewModel`/`RegisterViewModel`/`AuthViewModel` (StateFlow).
- **Launch flow:** custom animated Compose **splash** (mirrors iOS `SplashView`) held until
  **animation finished AND `validate()` resolved** — fixes the "ghost session" deterministically.
  `RootScreen` swaps Home/auth-graph by `AuthStatus`.
- **Custom `Toast`** composable (top, slide+fade, 3s auto-dismiss) replacing `Snackbar`, to match
  the iOS `ToastModifier`.
- `HomePlaceholderScreen` with sign-out.
- Fix: `BackHandler` on Welcome finishes the activity (back on the root no longer leaves a black screen).

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
    **Promote to a real `ViewModel` + DI when networking lands (milestones 2–3).**
    *(Done on 2026-06-27 — promoted to `ViewModel` + StateFlow with **Koin**, not Hilt. See below.)*
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

> Note: the real package is `com/lucasdias/gametrackr/`. DI is Koin (`di/AppModule.kt`),
> not Hilt; config lives in the repo-root `config/` folder (see the 2026-06-27 log).

```
app/src/main/java/com/lucasdias/gametrackr/
├── GameTrackrApp.kt              # Application — startKoin { modules(appModule) }
├── di/
│   └── AppModule.kt              # Koin module: clients, APIs, repo, ViewModels
├── core/
│   ├── network/
│   │   ├── AuthApi.kt / RefreshApi.kt
│   │   ├── AuthInterceptor.kt / TokenAuthenticator.kt
│   │   ├── ApiError.kt           # typed error model + ErrorMapper
│   │   └── dto/                  # @Serializable DTOs
│   ├── auth/
│   │   ├── TokenStore.kt         # DataStore (Preferences)
│   │   ├── SessionManager.kt     # StateFlow<AuthStatus>
│   │   └── AuthRepository.kt
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

Paste **`CLAUDE.md` + this file**, then add an instruction, e.g.:

> "Based on this context, set up the Retrofit `ApiClient` with an `AuthInterceptor` and a typed `ApiError` mapped from the API's 422 shape."

> "Based on this context, I'm on milestone 5. Implement `LibraryRepository`, `LibraryViewModel` (StateFlow), and the Compose list with a status filter."

> "Based on this context, review this ViewModel and point out what a senior Android dev would change."
