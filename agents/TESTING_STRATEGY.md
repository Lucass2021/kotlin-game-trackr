# GameTrackr — Testing Strategy (Android)

> Companion to `CLAUDE.md` (shared product/API context) and `agents/PROJECT_GOALS.android.md`.
> The iOS repo carries the mirrored version of this file at the same path.
> **Written 2026-09-05**, right before the big refactor, so the tests exist *before* the code moves.

---

## Why now

Every route the API exposes is wired on both clients. The next step is a refactor — which is
exactly the moment a codebase without tests silently regresses. The goal here is **not** a
coverage number; it is a **safety net shaped like the bugs this project actually had**.

Baseline at the time of writing: **0 real tests** (only `ExampleUnitTest.kt` /
`ExampleInstrumentedTest.kt` from the template).

---

## Principle: test by risk, not by file

The instinct — "one unit test per component" — produces a lot of tests that assert a `Text` is on
screen and catch nothing. Instead, every test in this project should be traceable to one of:

1. **A bug that already happened** (they are all catalogued in `CLAUDE.md`).
2. **A contract with a backend we don't own** (IGDB shapes, Laravel error bodies).
3. **A state machine** (loading / loaded / error / paginating).

If a test doesn't fit one of those three, it's probably not worth writing.

### The bug catalogue → the test list

These are real regressions this project shipped. Each one becomes a test:

| Bug that happened | Test that would have caught it |
| --- | --- |
| `auth/validate` returned `200 {"user": null}` on an expired token → random logouts | Session bootstrap must use `/profile/me` and must log out on 401 |
| Public routes personalise from the token and never 401 → joined community rendered as "Join" | `AuthInterceptor` refreshes proactively when `isJwtExpired`, before the request goes out |
| `DELETE /communities/{id}` answers **401 for "not the author"** — same code as an expired token | 401 **with** an `error` key → `ApiError.Forbidden`; 401 **without** → `Unauthorized`; `TokenAuthenticator` must skip the refresh in the first case |
| Page 1 duplicated while paginating | Reset path sets loading; `InfiniteScrollEffect` can't fire on an empty layout |
| IGDB omits absent keys instead of sending `null` | Decode a payload with no `cover` / no `first_release_date` |
| `first_release_date` is a Unix timestamp, not ISO 8601 | `toDomain()` date mapping |
| `meta` is nested and keyed `page` (unlike the flat Laravel `current_page`) | Both paginated shapes decode into the same model |
| Duplicate community name → **500 with raw SQL**, not 422 | 500-on-create maps to "That name may already be taken." |

---

## The five layers

### Layer 1 — Pure logic · target 90–100%

Plain JVM tests in `app/src/test`, no emulator, milliseconds. **Start here.**

- `Jwt.isJwtExpired` — leeway, malformed token, missing `exp`, base64url padding
- `toApiError()` — the whole `when`, and especially the 401-with-`error` branch
- `toDomain()` on every DTO — missing keys, Unix timestamps, platform slugs
- Paginated DTO decoding — both the nested `meta.page` and the flat `current_page` shapes
- `PaginationState` — `canLoadMore`, `append`, `reset`, `restore`, index guards
- `FeedCache` — 5-minute TTL, key identity (scope + search + platform)
- ViewModel validation (name ≥ 3, email, password ≥ 6, confirm match, terms)
- `EditProfileViewModel` — username charset, length limits, `hasChanges`
- Community handle derivation (the whitespace-stripping the backend does)

### Layer 2 — ViewModels against a fake API · target ~80%

Assert **state transitions**, not pixels: `isLoading` → `games` → `hasError`, the `force` guard,
the pagination reset on filter change, the optimistic like/join rollback on error.

Koin already injects every ViewModel through its constructor (`HomeViewModel(private val api:
GameApi)`), so a test just passes a fake `GameApi` — **no Koin needed in the test at all**.

- `kotlinx-coroutines-test` — `runTest` + `StandardTestDispatcher`; set `Dispatchers.Main` with a
  `MainDispatcherRule`, otherwise `viewModelScope` throws.
- `Turbine` — assert the sequence of `StateFlow` emissions instead of sampling `.value` and hoping.
- `MockK` — only where a hand-written fake would be noise. Prefer real fakes for the API interfaces.

### Layer 3 — Contract / decoding tests ⭐

The highest-value non-unit layer for this project, because the API is owned by someone else and
IGDB shapes are hostile.

- Real JSON captured from the running API lives in `app/src/test/resources/fixtures/`.
- **MockWebServer** replays it against a real Retrofit + kotlinx.serialization stack — no network,
  no backend running, safe in CI.
- This is where `AuthInterceptor` / `TokenAuthenticator` / `TokenRefresher` get tested for real:
  proactive refresh on an expired JWT, refresh-on-401, the body-peek that skips a pointless
  refresh on a "not the author" 401, and the single-flight guard (two concurrent 401s must produce
  **one** refresh call).

**Re-capture the fixtures whenever the backend changes.** A stale fixture is worse than no test.

### Layer 4 — Snapshot tests · ~20 components

The cheap replacement for E2E, and the actual safety net for the refactor.

- **Paparazzi** (or Roborazzi) — renders Compose **on the JVM, no emulator**, so it runs in the
  same fast `test` task as everything else. This is why it beats `ui-test-junit4`, which needs a
  device.
- Cover only shared components with real states: `GameCoverArt` (the three states: loading /
  no-artwork gradient / loaded), post card, platform chips, profile header, empty states.
- **Do not snapshot** whole screens, or anything animated — `subtleBounce` / `subtlePulse` make
  snapshots flake.
- Paparazzi needs the fonts (Sora/Inter) resolvable at test time; register them in the test setup.

### Layer 5 — What we deliberately do NOT test

- **E2E / Espresso flows** — rejected: too slow, too flaky, too expensive to maintain for a
  practice project. Layer 4 covers the visual regression risk at ~1% of the cost.
- `AppModule.kt` wiring, design tokens (`AppType`, colors), `@Preview` bodies, trivial extensions.
- Anything mock-backed that has no API yet (Library, My Setup) — it will be rewritten when the
  endpoints land. Test it *after* it talks to the backend.

Exclude all of the above from the coverage metric, or the number lies.

---

## Coverage

- **Kover** (`org.jetbrains.kotlinx.kover`) — better Kotlin support than JaCoCo (inline functions,
  coroutines, `data class` synthetics). Run `./gradlew koverLog` for the number,
  `./gradlew koverHtmlReport` for the drill-down.
- ⚠️ **Kover must be 0.9.6 or newer.** 0.9.1 cannot read AGP 9's variant API: it configures without
  error, exposes only the "for all code" tasks, and every report prints `No sources`. It looks like
  a filter mistake and is not one.
- ⚠️ **The Kover percentage is not comparable to the iOS one.** Kover excludes `@Composable`;
  `xccov` on iOS has no filter and counts every View. Compare per-package rows, not the totals.
- **Phase 1 and 2: measure, don't gate.** Get a real number first.
- Gate only once it's stable, and gate by package, not globally:
  - `core.network`, `core.auth`, `core.pagination`, `core.model` → **90%**
  - `feature.**.*ViewModel` → **80%**
  - Composables → **not measured** (exclude `**/*Kt.class` for screen files, `*_Preview*`)
- Coverage is a smoke detector, not a goal.

---

## Benchmarks — capture the baseline BEFORE the refactor

Android's tooling here is genuinely good; this is the platform where benchmarking pays off.

- **Macrobenchmark** module (`:benchmark`, runs against a `release` build):
  - `StartupTimingMetric` — cold start.
  - `FrameTimingMetric` — jank while scrolling Home and Search. **This is the one that matters**:
    startup is dominated by framework init, but a refactor of Compose state directly changes
    recomposition and therefore frame times.
- **Baseline Profiles** — generate them alongside. Unlike the metrics, this one *improves* the app
  (typically 10–30% off startup), so it's worth the setup on its own.
- Microbenchmark: skip it. There is no hot algorithm in this codebase to justify it.

**Rules so the numbers mean something:**

- Always the **same physical device**, never the emulator — emulator variance (30%+) makes the
  number noise.
- Record results in `agents/BASELINE.md` with date, device, Android version and app version.
- **Never gate CI on a benchmark.** Use it as a manual before/after comparison.
- ⚠️ `FeedCache`'s 5-minute TTL will falsify any feed benchmark — the second run makes no request.
  Clear it in the setup, or pass `force = true`.

---

## Dependencies to add

Already wired (2026-09-05): `kotlinx-coroutines-test` 1.9.0, `turbine` 1.2.0, `mockk` 1.13.13,
`mockwebserver` (pinned to the app's okhttp 4.12.0), and the `kover` 0.9.6 plugin.

Still to add when their phase arrives: `app.cash.paparazzi` (Phase 4) and
`androidx.benchmark:benchmark-macro-junit4` (Phase 2).

### Test harness in place

```
app/src/test/java/com/lucasdias/gametrackr/
  support/
    MainDispatcherRule.kt   swaps Dispatchers.Main for a TestDispatcher
    FakeGameApi.kt          canned GameApi + call counter + optional suspension gate
    TestData.kt             DTO builders
  feature/app/home/
    HomeViewModelTest.kt    load, loading emission, error, the force guard
```

Two things worth remembering when adding more:

- **`Dispatchers.Main` does not exist on the JVM.** Every ViewModel test needs
  `MainDispatcherRule`, or `viewModelScope.launch` throws — it reads like a broken test, not a
  missing rule.
- **`StateFlow` conflates, so an intermediate `isLoading` emission is not observable** when the
  fake returns instantly: Turbine sees the initial and final states only. `FakeGameApi` therefore
  takes an optional `CompletableDeferred` gate — park the coroutine on it, assert the loading
  state, then complete it. Use the gate for anything that asserts an in-flight state.

---

## Roadmap

| Phase | What | Status |
| --- | --- | --- |
| **0** | Dependency injection so ViewModels/services are constructible with fakes | ✅ **already done on Android** — Koin injects through the constructor. Only iOS needed this phase. |
| 1 | Layer 1 tests + Kover measurement (no gate) | ▶ tooling in place 2026-09-05 |
| 2 | Benchmark baseline recorded in `agents/BASELINE.md` | |
| 3 | Layers 2 and 3 (ViewModels + MockWebServer/fixtures) | |
| 4 | Layer 4 Paparazzi snapshots of shared components | |
| 5 | The refactor itself, with the net in place; turn the Kover gate on after | |

> **Why Android skips Phase 0 and iOS doesn't:** Koin pushed dependencies in through the
> constructor from day one. iOS reached for singletons (`GameService.shared`, `APIClient.shared`,
> `KeychainHelper` statics), so no iOS ViewModel was constructible with a fake. That asymmetry is
> the whole of Phase 0 — see the iOS repo's `agents/TESTING_STRATEGY.md`.
