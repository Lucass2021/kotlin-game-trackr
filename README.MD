# GameTrackr — Android

Native **Kotlin + Jetpack Compose** client for GameTrackr, a social game-tracking app (think
Backloggd / Letterboxd, but for video games). This repo is the **Android half**; an iOS app is
built in parallel against the same backend, and a Vue web app + Laravel API are built by a
collaborator.

The point of the project is **practice** — building the same product natively on two platforms
against one real backend, so the focus stays on idiomatic Compose rather than re-inventing
business logic.

## Docs

| File | What's in it |
|------|--------------|
| `CLAUDE.MD` | **Shared** product + API contract. Identical in the iOS repo — edit both together |
| `agents/PROJECT_GOALS.android.md` | Android stack, architecture, milestones, and the progress log |
| `TODO.MD` | Android-specific backlog (the cross-platform one lives in the iOS repo) |

The iOS repo also holds the shared status docs: `agents/api-integration-status.md`,
`agents/missing-screens.md`, and `agents/testing-community-integration.md`.

## Stack

- Kotlin, Jetpack Compose + Material 3 (kept neutral so the design matches iOS)
- MVVM with a repository layer, unidirectional state via `StateFlow`
- DI: **Koin** — not Hilt. Hilt's Gradle plugin doesn't support this project's **AGP 9.0.0**;
  Koin needs no Gradle plugin
- Networking: Retrofit + OkHttp + `kotlinx.serialization`
- Auth: **JWT**, token in **DataStore (Preferences)** — EncryptedSharedPreferences is deprecated,
  so unlike iOS's Keychain this is **not encrypted at rest**
- Config: `config/` properties folder (`debug` / `release` / gitignored `local`) →
  `BuildConfig.API_BASE_URL`
- Icons: Phosphor, via a shared `AppIcon` enum
- Type: Sora (headlines) + Inter (body) as variable fonts, mirroring iOS. Use the `AppType`
  tokens — never raw `fontFamily` / `fontSize` / `fontWeight` on a `Text`
- Tooling: ktlint (via Lefthook pre-commit)

## Running it

Open the project in Android Studio and run on an emulator, or:

```bash
./gradlew installDebug
```

The app boots to Welcome; you can sign in, register, or **continue as guest** (read-only, no
token, no backend call).

For the live Community/Posts integration you need the backend running locally:

```bash
cd ~/Projetos/game-trackr-api && docker compose up -d
```

The emulator reaches the host through **`10.0.2.2`**, not `localhost` — already set as the debug
base URL. On a physical device, point `API_BASE_URL` at your Mac's LAN IP instead.

## Current state

Auth and Community/Posts talk to the real API. Everything else — Home/discovery, Library, Stats,
Notifications, Achievements, My Setup — renders from **mock data**, because those endpoints don't
exist yet. The iOS repo's `agents/api-integration-status.md` is the authority on which is which.
