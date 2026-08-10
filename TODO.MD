# Code quality
- Tests — still the Android Studio template (`ExampleUnitTest.kt` / `ExampleInstrumentedTest.kt`)
- Remove magic numbers CreateTopicViewModel.kt
- `CommunityEmptyState` is used outside Community (Library, My Setup) — rename to `AppEmptyState`
- `SetupPhoto.kt` decodes with `BitmapFactory` by hand — revisit if/when Coil is added for game
  covers, so there's only one image path in the app
- ktlint's `no-unused-imports` doesn't catch imports orphaned by deleting a `LazyColumn` `item {}`
  block — check imports by hand after removing a section

# API integration (blocked — no backend yet)
- **My Setup / Collection** — UI is done but data is in-memory only and lost on restart. Needs
  `GET/POST/PATCH/DELETE /me/collection` + image upload. Swap point is the hoisted state holder
  in `MainTabScreen`.

> Community + Posts are **already wired** to the real API (`CommunityApi`) on both clients.
> The rest of the cross-platform backlog (blocked endpoints, missing screens) lives in the iOS
> repo's `TODO.MD` and `agents/api-integration-status.md` — both apps share it.
