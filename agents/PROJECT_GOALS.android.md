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
- OkHttp `Interceptor` attaches the Bearer token; OkHttp `Authenticator` handles `401`. Both share
  one `TokenRefresher` so the proactive and reactive paths can't drift.
- **Verified backend behaviour:** auth is **JWT (tymon/jwt-auth), not Sanctum**, using
  **single-token rotation** — on `401`, call `POST /auth/refresh` with the *current* token in
  the header (no body); it returns a new token and blacklists the old one. There is **no separate
  refresh token**. The `Authenticator` does: retry-once + concurrent-refresh **dedup**
  (single-flight) + **network-failure ≠ auth-failure** (no logout when offline; only logout when
  refresh genuinely fails / token is dead).
- **Reacting to `401` is not enough on this backend** (learned 2026-08-25). Two traps:
  - *Public routes personalise from the token and answer `200`.* `GET /communities` fills
    `is_member` from `auth()->id()` while sitting outside `auth:api`, so an expired token yields
    `is_member: false` for everything — a joined community renders as "Join" and no `401` ever
    arrives to trigger a refresh. `AuthInterceptor` therefore checks `isJwtExpired()` and refreshes
    **before** sending, not only after failing.
  - *Not every `401` is about the token.* A controller denying an action answers
    `401 { error: … }` while the auth middleware answers `401 { message: "Unauthenticated." }`.
    `ErrorMapper` branches on the body into `ApiError.Forbidden` vs `ApiError.Unauthorized`, and
    `TokenAuthenticator` peeks the body to skip a refresh that can never help.
- Bootstrap the session with **`GET /profile/me`**, never `POST /auth/validate` — the latter is
  outside `auth:api` and hands an expired token `200 { "user": null }`, which the repository turned
  into a failure and the ViewModel into a **random logout**.

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
| 3 | Auth screens wired to real API (login, register, token persistence) | a weekend — **Google sign-in added 2026-09-05** |
| 4 | Token refresh with single-flight guard (if backend uses refresh) | + half a day |
| 5 | Library: list with status filter, add game, edit entry | + 1 day |
| 6 | Game search + detail → add to library | + 1 day — **global search + detail done 2026-09-05**; "add to library" blocked on the Library API |
| 7 | Profile + stats + sign out | + half a day — **edit profile persists 2026-09-05** (name/username/color); stats still mock |
| 8 | Discovery feeds (releases / upcoming / trending) | + 1 day — **releases done 2026-08-19**; upcoming/trending blocked on the API |
| 9 | Friends + public profiles | + 1 day |
| 10 | Realtime messaging via Reverb + push notifications | + 2 days |
| 11 | Community + physical collection (image upload) | + 2 days — **community create/delete done 2026-08-25**; collection still local-only |
| 12 | Polish: states, animations, app icon, CI | + 1 day |

---

## Progress log

### 2026-09-05 — Google sign-in, API-driven platform chips and colors, global search

Five things shipped, and after them **every route the API exposes is wired**. What is left is not
unwired — it does not exist server-side (Library, My Setup, friends, messaging, notifications, feed).

**Google sign-in (`/auth/google/redirect` → `gametrackr://auth/callback`).** The backend owns the
whole OAuth dance; the client only opens a browser and catches a JWT off the callback URL. Then
`GET /profile/me` turns that token into a session, and a failure clears the token instead of leaving
a half-session behind.

The parameter matters: the backend reads `state` / `platform` / `is_mobile`, **not `client`**. Both
apps sent `?client=mobile` at first, which fell through to the `web` branch and redirected to the Vue
frontend — the deep link never reached the app. Sending `?platform=mobile` is what makes the callback
come back as `gametrackr://`.

**`GET /platforms` → the filter chips.** The route used to answer `[]` (it filtered on `category`,
renamed to `platform_type` in IGDB v4); the backend now falls back to a curated list of 6. The chips
went from 4 hardcoded *families* to whatever the API returns, so **no platform data is hardcoded in
either client any more** — `GamePlatform` is a plain `{id, slug, name}` decoded from the response,
and a failed request simply renders no chips (no local fallback, on purpose). The cost: the curated
list has no `switch-2` or `ps3`, so those are not filterable until the backend adds them.

**Most Anticipated cards stopped cropping.** They were a 260×150 landscape box holding a 3:4 portrait
cover, so titles like Fable lost half the art. The feed payload carries **only `cover`** — there is
no landscape asset to switch to — so the card became a 170×227 portrait, larger than New Releases and
still marked by the year badge. Any height would only have chosen *how much* to crop.

**`PATCH /profile` + `GET /profile/colors` — the first user data the app actually persists.**
`Profile` now stores `avatarHex` and *computes* `avatarStart`/`avatarEnd` (hex darkened 28%), because
the API sends **one hex per color**, not a gradient pair. The selected swatch is matched by hex
string, not by index or enum: the current color arrives from `/profile/me` and the list from
`/profile/colors`, two independent responses whose only shared value is the hex. `bio` and
`visibility` stay mock — the API has no column for either.

The old `AvatarPalette` (7 named gradients) turned out to also paint the **My Setup** cards; deleting
it broke the build. It was renamed `SetupPalette` and moved into the setup feature, which is what it
had actually been doing.

**`GET /games/search` — the magnifying glass in the global header.** Search used to reuse
`/home/new-releases/all?search=`, so it could only find games inside the recent-releases slice.
It now hits the whole catalog: 315k games, Elden Ring and Skyrim included. With an empty query the
route sorts by `total_rating_count desc`, so the header reads **"Popular"** — calling it "Recent
Releases" would have described the wrong list. The two Home "View all" targets keep their feed
endpoints.

**Verified** end-to-end on simulator and emulator against the local API, including cross-device:
saving Emerald on iOS wrote `#10B981` and the Android profile opened green; changing it to Amber on
Android wrote `#F59E0B`.

**Environment note.** `JWT_TTL=60` was added to the API's `.env` — it was unset, and `config/jwt.php`
defaults it to **1 minute**, which made every local session look like it dropped at random.

### 2026-08-25 — Create/delete community, and the two `401` traps behind it

**Feature.** The last two unwired community routes shipped: `POST /communities` and
`DELETE /communities/{id}`.

- New `feature/app/community/createcommunity/` — `CreateCommunityScreen` +
  `CreateCommunityViewModel` + `CreateCommunityUiState`, reached through a new
  `ShellRoutes.CREATE_COMMUNITY`. The `+` FAB now lives on **both** segments and changes intent with
  the segment (`Feed` → New post, `Discover` → New community); `CreatePostButton` took a `label`
  parameter for that. Creating one adds it to the list and navigates straight into its detail
  (`popUpTo(CREATE_COMMUNITY) { inclusive = true }`), already joined.
- Delete lives on `CommunityDetailScreen`, owner-only (`community.authorId == currentUserId`).
  `Community` gained `authorId`, and `CommunityPost` gained `communityId` (the DTO always had it;
  `toDomain()` was dropping it) so the feed can be pruned when a community goes away.
- `CommunityApi` gained `createCommunity` / `deleteCommunity`; `CommunityDtos` a
  `CreateCommunityRequest` / `CreateCommunityResponse`.

**Backend quirks that shaped the UI.** `store` runs `str_replace(' ', '', $title)` but computes the
`slug` from the raw text *first* — which makes the slug a perfect probe for what the client actually
sent. So the client sends a **handle** (typed name minus whitespace) and shows it live under the
field: "Listed as `RetroShmupClub`". And `CommunityRequest` has no `unique` rule though the column
does, so a duplicate name is a **500 with raw SQL**, mapped to "That name may already be taken."

Compose has no trouble filtering text in `onValueChange`, but both clients use the handle approach so
they behave identically — on iOS a `TextField` keeps input the binding rejects, which made live
filtering unusable there.

**UX correction.** The overflow `…` was too hard to find: it reused the outlined `CircleIconButton`
while the Back button beside it is a filled translucent circle, so it disappeared into the banner
gradient. Both now share one `FloatingCircle`, and the real affordance is a labelled
**"Delete community"** row at the bottom of the **About** tab (`CommunityAboutSection`, owner-only,
with "Only you can see this"). A bare glyph is not an affordance for a destructive action.

**The bug under the bug.** Chasing "the delete button never appears" led to
`AuthRepositoryImpl.validate()` calling `POST /auth/validate` — a route outside `auth:api` that
answers an expired token with `200 { "user": null }`. Deserialisation failed, the ViewModel read that
as a dead session and called `setUnauthenticated()`: the **random logouts** seen all session.
Switched to `GET /profile/me`, and added the proactive-refresh and `Forbidden`-vs-`Unauthorized`
rules described under *Auth interceptor*. New `core/network/Jwt.kt` decodes `exp` client-side and
`core/network/TokenRefresher.kt` is now the single place a spent token is swapped.

**Verified** end-to-end on the emulator against the local API, including with a deliberately expired
token: the Discover list keeps the right `Joined` state, the owner controls appear, and deleting
refreshes and succeeds instead of reporting a permission error. Note `JWT_TTL` is **1 minute**
locally (`config/jwt.php` defaults to 1, `.env` doesn't set it), which is why these traps fire
constantly here.

### 2026-08-24 — Game detail, Most Anticipated, server-side search and the feed cache

Shipped in `f84276f` and `2635686`. `GET /games/{slug}` fills the whole detail screen from one
request (hero picks screenshot → artwork → cover, since artworks are sometimes solid black), and the
Specifications list renders one full-width card per row. `GET /home/most-anticipated` (+ `/all`)
reuses the Search screen with a scope, with a landscape card and a year badge. Search and the
platform chips now filter **server-side** via `?search=` and repeated `?platform[]=`, debounced
300 ms only when the typed text changed, with a generation counter dropping stale responses. Feeds
are cached in memory per `FeedKey(scope, search, platform)` for 5 minutes (`FeedCache` +
`PaginationSnapshot`), so re-tapping a chip or coming back from a detail screen is instant. The
System Requirements section was deleted — IGDB has no such data and it was invention.


### 2026-08-19 — New Releases wired to the real IGDB feed (first discovery data)

- **The first non-auth, non-community feature to leave mock data.** `GET /home/new-releases`
  drives the Home row and `GET /home/new-releases/all` drives the "View all" grid with real
  pagination. Both routes are **public**, so a guest session sees real games — `AuthInterceptor`
  simply finds no token and sends none.
- **New data layer:** `core/network/dto/GameDtos.kt`, `core/model/Game.kt` (domain),
  `core/network/GameApi.kt` registered in `AppModule`, plus `HomeViewModel` and `SearchViewModel`
  (both `koinViewModel()`).
- **`PaginatedGamesResponse.toPaginated()`** is the seam that matters: it maps IGDB's nested
  `meta { page, per_page, total, last_page, has_more }` onto the flat `PaginatedResponse` the
  community endpoints return, so `PaginationState` powers both feeds unchanged.
- **Coil 3 added** (`io.coil-kt.coil3:coil-compose` + `coil-network-okhttp`) — the app's first
  remote-image loader. Import is `coil3.compose.AsyncImage`, **not** `coil.compose`. `GameCoverArt`
  gained a `url:`; because the `Box` already clips, `ContentScale.Crop` + `fillMaxSize()` is all it
  takes (iOS needed an overlay trick for the same result). The gradient stays as the placeholder.
- **One model for every game surface.** `NewRelease` and `SearchGame` collapsed into `Game`;
  `SearchMockData` was deleted. `GamePlatform` moved from the Search feature to `core/model`.
- **A guessed contract bit me.** IGDB's platform slugs are `ps4--1`, `switch-2` and `series-x-s`,
  not the obvious spellings. The abbreviation map silently fell back to the full platform name, so
  nothing failed — it just rendered "PlayStation 4" instead of "PS4". Only the real response caught
  it. Fallbacks hide contract bugs from builds *and* from tests.
- **Search screen reworked (same day).** The "Can't find a game?" banner is gone (a dead end under
  an infinite list), and the platform chips no longer flash "No games found": filtering only sees
  the pages already downloaded, so when a filter empties the result the screen pulls up to 5 extra
  pages behind a spinner before admitting a real miss.
- **`LaunchedEffect` key foot-gun.** The auto-fetch keys first included `pagination.isLoadingMore`;
  that flag flips the instant a fetch starts, so the effect relaunched against a request already in
  flight. Never key an effect on state the effect itself mutates.
- **`InfiniteGridScrollEffect` fired before any scroll, and it was NOT harmless.** On first
  composition `layoutInfo` is empty, so `0 >= 0 - threshold` was true. It raced the initial
  `loadNewReleases(reset = true)`, which sets `_isLoading` but never `pagination.setLoading(true)` —
  so `canLoadMore` still read `true`, `nextPage` was still `currentPage + 1 = 1`, and **page 1 was
  fetched twice and appended twice**. Every item appeared duplicated; the same Xbox filter showed 13
  results on Android against 6 on iOS. iOS escaped only by accident: its load-more lives in the
  cards' `onAppear`, which cannot fire before the first page exists.
  Fixed on both sides: the shared effects bail out when `totalItemsCount == 0` or there are no
  visible items, and the reset path now marks the pagination busy. The same race existed in
  Community (`CommunityViewModel` has the identical reset path) and the shared fix covers it.

### 2026-08-10 — My Setup (first real device photos), Discover cleanup, overflow-menu audit

- **My Setup** (`feature/app/profile/setup/`) — the profile's Setup section became a real feature:
  `MySetupScreen` (list + empty state) → `EditSetupScreen` (add/edit form) → delete with
  `AlertDialog`. `ProfileSetupRow` now has an inline empty card and an "Add setup" tile at the end
  of the `LazyRow`. `SetupCover` renders the first photo, falling back to the palette gradient.
  Routes `MY_SETUP` / `EDIT_SETUP` added to `ShellRoutes`.
- **First real images in the app — without adding Coil.** `EditSetupScreen` uses
  `ActivityResultContracts.PickMultipleVisualMedia(6)`, which returns `Uri`s. Rather than pull in
  Coil (an image *loading* library, built around network fetching) for a handful of local URIs,
  `SetupPhoto.kt` has a ~20-line `rememberSetupPhoto(uri)`:
  - `produceState` keyed on the `Uri` + `withContext(Dispatchers.IO)` so decoding never blocks
    composition.
  - Two-pass `BitmapFactory`: `inJustDecodeBounds` for the size, then `inSampleSize` doubled until
    the long edge is under 1200px. Chosen over `ImageDecoder` because **`minSdk = 24`** and
    `ImageDecoder` is API 28+ — this way there's no version branch.
  - Reconsider when game covers need real network images; then there'd be one path, not two.
- **No runtime permission needed.** `PickVisualMedia` uses the system photo picker
  ("This app can only access the photos you select") — it runs out-of-process, so nothing was added
  to the manifest. Verified end-to-end on the emulator: pick → decode → thumbnail → counter 1/6.
- **Persistence: none, deliberately.** `setups` is a `mutableStateListOf` in `MainTabScreen`,
  mirroring how `profile` already works. Data is lost on restart; that one holder is the swap point
  when `/me/collection` lands. The profile **starts empty** so the empty state is the default
  experience — `ProfileMockData.setups` survives for previews only.
- **`SetupItem` gained `id`/`photos`/`palette`** and dropped `imageStart`/`imageEnd`, reusing
  `AvatarPalette` (from `editprofile/`) for the placeholder gradient. Its picker was in the form
  briefly and was **removed** — the palette is assigned automatically, rotating by
  `AvatarPalette.entries[setups.size % size]`.
- **Discover:** dropped the "Featured" `LazyRow` and deleted `FeaturedCommunityCard.kt`. It showed
  `communities.take(3)` — the same three rows already visible below — and ignored the chip filter.
  Removing an `item {}` block orphaned `LazyRow`/`FontWeight`/`AppPrimary` imports; ktlint's
  `no-unused-imports` **did not** flag them, so check imports by hand after deleting a block.
- **Overflow (`…`) audit.** Three dead buttons removed: `UserProfileScreen` top bar,
  `PostDetailScreen` (non-`isOwnPost` branch, now a `Spacer(Modifier.size(40.dp))` so the centred
  wordmark stays on-axis), and `CommunityPostCard`. Also removed the one in `CommunityDetailScreen`,
  which let `BackCircle` lose its `icon` parameter — it had been passing `contentDescription =
  "Back"` for the overflow button, an accessibility bug that disappeared with it.
- **Empty-state centring:** `CommunityEmptyState` uses symmetric `padding(vertical = 60.dp)`, so
  wrapping it in `Box(Modifier.weight(1f), contentAlignment = Alignment.Center)` centres it
  directly. The iOS twin has asymmetric top-only padding and needs compensation — noted in TODO.MD.

### 2026-08-08 — Notification deletion, infinite scroll, share buttons, game detail cleanup

Batch of cross-platform feature work aligned with backend discussions.

- **Notification deletion** — **Clear all** via the top-bar overflow menu. The top bar now shows a
  three-dot overflow `DropdownMenu` with "Mark all read" + "Clear all" (destructive, red label +
  trash icon). Adds all IDs to the existing `removedIds` list.
- **New icon:** `AppIcon.TRASH` (Phosphor `Trash` / `TrashFill`) added to the shared icon enum.
- **Infinite scroll** on all 8 vertical lists (4 API-backed, 4 mock). New pagination infrastructure
  in `core/pagination/`: `PaginationState<T>` (real API), `MockPaginationState<T>` (sliced pages
  with 600ms delay), `LoadingMoreIndicator`, `InfiniteScrollEffect` / `InfiniteGridScrollEffect`
  (`derivedStateOf` to detect scroll near end, threshold=3). API lists (`CommunityScreen`,
  `CommunityDetailScreen`, `DiscoverCommunitiesContent`) track `currentPage`/`lastPage` from
  `PaginatedResponse`; mock lists (`SearchScreen`, `LibraryScreen`, `NotificationsScreen`,
  `AchievementsScreen`) use `MockPaginationState` with first page preloaded synchronously in
  `init` block. Network layer (`CommunityApi`) gained `@Query("page")` parameters.
  `CommunityViewModel` refactored to use `PaginationState` with backward-compatible computed
  properties (`val feed get()`, `val communities get()`).
- **Share buttons** across the app: `Context.shareText()` extension on game detail hero, profile
  (own + other user), stats top bar, community detail, and post detail. All share text-only messages.
  Fixed a double-tap bug by adding `navigateOnce()` guard to the share intent.
- **Game detail cleanup:** removed `GameCommunitySection` from game detail (community section
  doesn't belong on game pages per backend discussion).
- **Bug fix:** mock-paginated screens showed loading + empty state simultaneously — fixed by
  preloading the first page synchronously in `MockPaginationState`'s `init` block instead of
  async `LaunchedEffect`.

### 2026-06-29 — Password-reset wired to the real OTP backend + Remember-me removed

The backend shipped a real **3-step OTP** password reset (mobile branch). Replaced the
`AuthRepositoryImpl` mocks (`delay` + fake UUID token) with live Retrofit calls, following the
cross-client spec in the **sibling iOS repo**: `Projetos/GameTrackr/agents/password-reset-flow.md`
(iOS built first, mirrored here). Mirrors the iOS commit *"connecting routes in reset password flow"*.

- **Endpoints** (`AuthApi`): `forgot-password` · `verify-reset-code` · `reset-password` (all POST,
  no auth — `AuthInterceptor` only attaches a Bearer if a token exists, and there is none mid-reset).
  Request DTOs send **`client = "mobile"`** (`ForgotPasswordRequest`, `ResetPasswordRequest`). Since
  the shared `Json` has `encodeDefaults = false`, the constant is forced on with **`@EncodeDefault`**
  — otherwise it'd be omitted and the backend would fall into the web email-link branch.
- **No reset token.** `verifyResetCode` now returns `Result<Unit>` (was `Result<String>`); the
  verify screen carries **`email` + `code`** forward instead. Route changed
  `reset_password/{token}` → `reset_password/{email}/{code}`; `resetPassword(email, code, newPassword)`.
- **Verify is server-gated:** navigation to Reset only happens after `verify-reset-code` returns 200
  (`uiState.verified` flips → `LaunchedEffect` calls `onVerified(code)`). A wrong code stays put.
- **HTTP 400 mapping:** added `ApiError.BadRequest(message)` + a `400` branch in `ErrorMapper`
  parsing the `{ error }` body, surfaced verbatim in `ApiErrorMessage` — so "Invalid code" /
  "Code expired" / "Code already used" show as a real toast instead of the generic error.
- **Auto-login after reset** (mirrors the Register pattern): `reset-password` returns **no token**,
  so `resetPassword` stashes the credentials and `AuthRepository.completePasswordReset()` does a
  silent `login` → `SessionManager.setAuthenticated` → Home. The reset Success screen's `onPrimary`
  now calls `AuthViewModel.completePasswordReset()` (was `popBackStack` to Login).
- **Remember-me removed:** dead UI (never read; the JWT in DataStore already persists the session).
  `RememberMeRow` → `ForgotPasswordRow` (keeps only the trailing "Forgot my password" link);
  `rememberMe`/`onToggleRememberMe` dropped from `LoginUiState`/`LoginViewModel`/`LoginScreen`;
  `login_remember_me` string removed.
- **No `isResetFlowActive` flag** (the iOS equivalent): Compose's nav back-stack handles the chain,
  so the flag isn't needed here — see §4 of `password-reset-flow.md`.

> Backend prerequisites to test end-to-end (not Android code): **Mailtrap** must be configured to
> read the 6-digit code, and the `verified_at` **migration trap** (a column added by editing an
> already-applied migration) needs a `migrate:rollback` + `migrate` on an existing DB — both
> documented in §5 of `agents/password-reset-flow.md`.

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
