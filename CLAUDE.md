# GameTrackr Android (Kotlin / Jetpack Compose)

## Context
## This project is for learning Jetpack Compose; keep the code simple, small and easy to read
## The iOS twin lives in ~/Projetos/GameTrackr (SwiftUI); every refactor batch is done in both projects with the same scope
## Keep the exact same names in both projects (files, types, functions, properties, folders); differ only where a language convention requires it (e.g. Kotlin const SCREAMING_CASE, Swift PascalCase folders)
## Product context, screens and history live in sessions/ and agents/PROJECT_GOALS.android.md; reference designs in agents/screens-base-design/
## UX references: Reddit for community features (feed, posts, comments)

## Workflow
## Read the latest sessions/AAAA-MM-DD.md before starting; read older ones if needed
## Update the progress of each session in sessions/AAAA-MM-DD.md; these files must not be committed
## Never put credentials, tokens, keys, personal e-mails or private URLs in sessions/ or any committed file
## Work in small batches: change one thing, ask for review, and only move on after an explicit ok
## Leave changes unstaged; the user reviews and stages them in the IDE, and commits include only what is staged
## Do not move code between "staged changes" and "changes" without permission
## Create stashes as backups when needed
## For every batch, build an interactive HTML review page for the user (in Portuguese) in ~/Projetos/gametrackr-reviews/: write notes/<date>-<batch>.json, run tools/build_review.py (reads the uncommitted changes of both apps, untracked files included) and open the page; it lives outside both repos and is never committed

## Git
## Do not commit anything without permission; always work on new branches for commits and pull requests
## Do not push without permission
## Follow the project pattern for branch names (feat/, fix/, refactor/, chore/, test/) and commits (conventional commits, lowercase, in English)
## Avoid large commits; one commit per batch
## Do not mention Claude or AI anywhere; never add "Co-Authored-By: Claude" or similar lines
## Do not commit sessions/
## Do not modify CLAUDE.md or TODO.MD without permission; keep TODO.MD short, details go in sessions/

## Architecture (MVVM)
## View: XScreen.kt is a stateless-as-possible Composable that receives UiState and lambdas; it never calls repositories
## ViewModel: XViewModel.kt exposes one StateFlow<XUiState> and plain functions for user actions (onSubmit, onEmailChange...)
## UiState: XUiState.kt is a single immutable data class per screen
## Model: repositories (interface + Impl) own network/cache; DTOs and mappers stay in the data layer; domain models in core/model
## Mocks for screens without API live as Fake repositories (FakeXRepository + XMockData) and are reused by previews and tests
## Dependencies are injected through constructors and wired in Koin (di/); no singletons or service locators inside ViewModels
## Screen folders: feature/<area>/<screen>/ with XScreen, XViewModel, XUiState and components/
## Keep files under ~200 lines; extract components into components/ when a file grows past that
## Every component and screen has a @Preview
## Follow the project architecture and existing patterns whenever possible

## Code style
## Do not add comments
## All text must be in English: code, UI strings, commits, CLAUDE.md, TODO.MD, sessions/ and any other .md file
## Avoid magic numbers; use named constants (private const val in a companion object, or dimension/time tokens in core/ui)
## Use the theme tokens Spacing, Radius and Durations (core/ui/theme); off-scale values round to the nearest token (ties round down); one-off sizes become named private constants in their file
## Avoid hardcoded colors like Color(0xFFE36E1C); add missing colors to core/ui/theme/Color.kt
## User-facing text goes in res/values/strings.xml
## Form submit buttons are always enabled; on tap, validate and show what is missing instead of disabling the button

## API
## The backend lives in ~/Projetos/game-trackr-api; use it only as reference, never change its code (Docker setup excepted, with permission)
## Always run git fetch --all in the backend before analyzing it, to see the latest code
## The API runs locally via Docker on port 8000; the emulator reaches it at 10.0.2.2
## If every request fails as "No internet connection", check for a leftover emulator proxy (adb shell settings get global http_proxy; clear it with :0)

## Tests
## Keep one unit test for each ViewModel, mapper and component logic
## Respect the lefthook pre-commit hooks (ktlint format, lint and tests); fix failures instead of skipping them

## Tools
## If claude-in-chrome is used, do not modify anything sensitive (e.g. Figma)
