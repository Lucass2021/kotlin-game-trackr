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

### Sign in with Google

The backend owns the OAuth flow; the app just opens a browser and catches a JWT off the
`gametrackr://auth/callback` deep link. It needs three keys in the API's `.env`
(`GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `GOOGLE_REDIRECT`) from a **Web application** OAuth
client in the Google Cloud Console, and your account added under *Test users* — otherwise Google
answers `403 access_denied`.

⚠️ **Google sign-in can't use `10.0.2.2`.** Google only accepts the registered redirect URI, and
`10.0.2.2` is an emulator-only alias it will never allow. You don't need a tunnel, though — run

```bash
adb reverse tcp:8000 tcp:8000
```

and set `config/local.properties` to `API_BASE_URL=http://localhost:8000/api`. That forwards the
device's own loopback to your Mac, so Chrome inside the emulator resolves the same
`http://localhost:8000` that is registered with Google. Re-run it after every emulator restart.

⚠️ `config/jwt.php` defaults `JWT_TTL` to **1 minute**, so if the API's `.env` doesn't set it, local
sessions expire almost immediately. Set `JWT_TTL=60` there — that is the environment, not the app.

## Current state

Auth (including **Sign in with Google**), **Home/discovery** (New Releases, Most Anticipated,
global catalog search, API-driven platform filters, game detail), **Community/Posts** — including
creating and deleting a community — and **Edit profile** talk to the real API. As of 2026-09-05
**every route the API exposes is wired**; what is missing is missing server-side.

Library, Stats, Notifications, Achievements and My Setup still render from **mock data** because
those endpoints don't exist: there is no `/me/library` route at all, and `/me/collection` was never
built. On the Edit profile screen, `bio` and `visibility` are the two fields that still don't
survive a restart — the API has no column for either. `CLAUDE.MD` marks each route with its real
status.
