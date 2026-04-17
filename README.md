# Game Radar

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Coinollar Logo" width="120"/>
</p>

<p align="center">
  <a href="https://github.com/farid-moghadam-dev/Game-Radar/actions/workflows/ci.yml"><img src="https://github.com/farid-moghadam-dev/Game-Radar/actions/workflows/ci.yml/badge.svg" alt="CI" /></a>
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack_Compose-2025.10-4285F4?logo=jetpackcompose&logoColor=white" alt="Compose" /></a>
  <a href="https://developer.android.com/tools/releases/platforms"><img src="https://img.shields.io/badge/minSdk-24-3DDC84?logo=android&logoColor=white" alt="Min SDK" /></a>
  <a href="https://developer.android.com/tools/releases/platforms"><img src="https://img.shields.io/badge/targetSdk-35-3DDC84?logo=android&logoColor=white" alt="Target SDK" /></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT" /></a>
</p>

A modern Android app for discovering video games, powered by the [RAWG Video Games Database](https://rawg.io/apidocs). Browse a paginated catalog of games, open rich detail pages with ESRB ratings, Metacritic scores, platforms, stores and descriptions, and drill into curated lists of **Platforms**, **Publishers**, **Developers**, **Genres**, and **Stores** to see all the games each one is associated with.

Built end-to-end with **100% Kotlin** and **Jetpack Compose**, following a **Clean Architecture + MVVM** layering and leaning on a modern, fully-coroutine-based stack (Ktor, Koin, Paging 3, Coil). Ships with R8 minification, static analysis (Spotless + Detekt + Android Lint), unit tests (MockEngine, Turbine, kotlinx-coroutines-test), a Compose UI test, and a GitHub Actions pipeline.

---

## Features

- **Paginated home feed** of popular games with a rotating image carousel header
- **Rich game detail screen** — metacritic score, ESRB age rating with tooltips, release date, rating, developers, publishers, genres, platforms, stores, tags, and an expandable description
- **Generic entity browser** — a single polymorphic list + detail flow serves all five RAWG catalog endpoints (Platforms, Publishers, Developers, Genres, Stores)
- **Cross-linking** — from an entity detail, tap any game to jump to its full details
- **Offline-friendly image caching** via Coil with memory + disk policies
- **Animated Lottie splash screen**
- **Material 3 theming** with full dark / light color schemes
- **Edge-to-edge Compose UI** with a Modal Navigation Drawer and double-back-press-to-exit

## Screenshots

<p align="center">
  <img src="screenshots/Games%20List-Dark%20Mode.png"    width="240" alt="Home / games list"/>
  &nbsp;&nbsp;
  <img src="screenshots/Game%20Details-Dark%20Mode.png"  width="240" alt="Game detail"/>
  &nbsp;&nbsp;
  <img src="screenshots/Entity%20Details-Dark%20Mode.png" width="240" alt="Entity detail"/>
</p>

<p align="center">
  <em>Home · Game Detail · Entity Detail</em>
</p>

<details>
<summary><strong>More screens</strong></summary>

<p align="center">
  <img src="screenshots/Games%20List-Light%20Mode.png"                 width="220" alt="Games list (Light)"/>
  &nbsp;&nbsp;
  <img src="screenshots/Game%20Card%20Press%20Animation-Light%20Mode.png" width="220" alt="Game card press animation (Light)"/>
  &nbsp;&nbsp;
  <img src="screenshots/Nav%20Drawer-Light%20Mode.png"                 width="220" alt="Navigation drawer (Light)"/>
</p>

<p align="center">
  <img src="screenshots/Nav%20Drawer-Dark%20Mode.png"  width="220" alt="Navigation drawer (Dark)"/>
  &nbsp;&nbsp;
  <img src="screenshots/Platforms-Dark%20Mode.png"     width="220" alt="Platforms list"/>
  &nbsp;&nbsp;
  <img src="screenshots/Publishers-Dark%20Mode.png"    width="220" alt="Publishers list"/>
</p>

<p align="center">
  <img src="screenshots/Developers-Dark%20Mode.png" width="220" alt="Developers list"/>
  &nbsp;&nbsp;
  <img src="screenshots/Genres-Dark%20Mode.png"     width="220" alt="Genres list"/>
  &nbsp;&nbsp;
  <img src="screenshots/Stores-Dark%20Mode.png"     width="220" alt="Stores list"/>
</p>

</details>

---

## Architecture

```
┌─────────────────────────────────────────────┐
│              Presentation (UI)              │
│  Jetpack Compose • ViewModel • Paging UI    │
│  NavHost with type-safe @Serializable routes│
└────────────────────┬────────────────────────┘
                     │  UiState<T> / PagingData
┌────────────────────┴────────────────────────┐
│                   Domain                    │
│   Pure Kotlin models • UseCases • Repo IF   │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────┴────────────────────────┐
│                    Data                     │
│  Ktor Client • DTOs • Mappers • PagingSrc   │
│  Repository impl • safeApiCall wrapper      │
└────────────────────┬────────────────────────┘
                     │
                RAWG REST API
```

- **Presentation** depends only on **Domain**.
- **Data** implements the **Domain** repository contract.
- **DI (Koin)** wires the graph together at the edges, in `GameRadarApp`.
- `UiState<T>` is a sealed class (`Loading | Success | Error`) that travels from data → UI for one-shot calls; `PagingData<T>` handles paginated streams.
- One generic `GameEntityType` enum (Platforms / Publishers / Developers / Genres / Stores) drives a single reusable stack of DTO → mapper → repository → use case → view model → screen, so adding a new catalog endpoint is a one-line enum change.

### Module layout

```
com.faridev.gameradar
├── core/                       // app-wide bootstrap + helpers
│   ├── GameRadarApp.kt         // Koin startup, Timber
│   └── util/                   // activity, compose, ext, icon, toast helpers
├── data/
│   ├── model/                  // RAWG response DTOs
│   ├── mapper/                 // DTO → Domain
│   ├── paging/source/          // Paging 3 sources
│   ├── remote/                 // KtorClientFactory, GameApi
│   ├── repository/             // GameRepositoryImpl
│   └── util/                   // safeApiCall
├── domain/
│   ├── model/                  // Immutable domain models
│   ├── repository/             // GameRepository interface
│   └── usecase/                // Single-responsibility use cases
├── di/                         // Koin modules
└── presentation/
    ├── activity/{splash, main} // Entry points
    ├── common/{components,state,theme}
    └── feature/
        ├── home/               // Games list
        ├── detail/             // Game details
        ├── entity/list/        // Entity list (generic)
        ├── entity/detail/      // Entity details + games-by-entity
        └── NavRoutes.kt        // Type-safe nav graph
```

### Design patterns

| Pattern | Where it lives |
|---|---|
| **Clean Architecture** | Strict presentation → domain ← data boundary; domain has no Android/framework dependencies |
| **MVVM** | `ViewModel`s expose `UiState<T>` / `PagingData<T>` for reactive Compose UI |
| **Repository** | `GameRepository` interface in domain, `GameRepositoryImpl` wraps the Ktor API |
| **Use Cases** | Single-responsibility suspend operators per action (e.g. `FetchGameDetailsUseCase`) |
| **Dependency Injection** | Koin modules wire api, repository, use cases, and view models |
| **Sealed State** | `UiState<T>` (`Loading / Success / Error`) and typed `AppError` for exhaustive `when` branches |
| **Single Activity** | Compose Navigation with type-safe `@Serializable` routes for every screen |
| **Polymorphic Feature** | One `GameEntityType` enum drives five catalog endpoints through one shared stack |

---

## Tech stack

| Concern | Library |
|---|---|
| Language | **Kotlin 2.2** |
| UI | **Jetpack Compose** (BOM) + **Material 3** |
| Navigation | **Navigation Compose** with type-safe `@Serializable` routes |
| Pagination | **Paging 3** (runtime + compose) |
| Networking | **Ktor Client** (Android engine) + Logging + ContentNegotiation |
| Serialization | **Kotlinx Serialization** (JSON) |
| Images | **Coil 3** with disk + memory caching |
| Dependency Injection | **Koin** (+ koin-compose) |
| Animation | **Lottie Compose** |
| Logging | **Timber** |

---

## Project setup

### Prerequisites

- **Android Studio** Ladybug or newer (JBR / JDK 17)
- **Android SDK 35**, minSdk 24
- A free **RAWG API key** — grab one at <https://rawg.io/apidocs>

### 1. Clone

```bash
git clone https://github.com/<your-user>/game-radar.git
cd game-radar
```

### 2. Provide your API key

Copy the example file and fill in your values:

```bash
cp local.properties.example local.properties
```

Then edit `local.properties`:

```properties
sdk.dir=/path/to/your/Android/Sdk
API_KEY=your_rawg_api_key_here
```

> `local.properties` is gitignored. Never commit real keys. The key is injected into `BuildConfig.API_KEY` by `app/build.gradle.kts` and appended to every Ktor request as the `?key=` query parameter.

### 3. Run

Open the project in Android Studio and hit **Run**, or from the command line:

```bash
./gradlew :app:installDebug
```

The debug variant installs as `com.faridev.gameradar.debug` with the label **Game Radar Debug**.

---

## How the generic entity layer works

All five catalog endpoints on RAWG (`/platforms`, `/publishers`, `/developers`, `/genres`, `/stores`) share the same response shape. Rather than duplicate five nearly-identical feature packages, the app defines a single enum and reuses one stack of classes end-to-end:

```kotlin
@Serializable
enum class GameEntityType(
    val apiPath: String,
    val gamesFilterParam: String,
    val displayName: String
) {
    Platforms (apiPath = "platforms",  gamesFilterParam = "platforms",  displayName = "Platforms"),
    Publishers(apiPath = "publishers", gamesFilterParam = "publishers", displayName = "Publishers"),
    Developers(apiPath = "developers", gamesFilterParam = "developers", displayName = "Developers"),
    Genres    (apiPath = "genres",     gamesFilterParam = "genres",     displayName = "Genres"),
    Stores    (apiPath = "stores",     gamesFilterParam = "stores",     displayName = "Stores")
}
```

The navigation drawer iterates over `GameEntityType.entries` and routes to a type-safe `@Serializable` destination that carries the enum as an argument:

```kotlin
navController.navigate(NavRoutes.EntityList(type))
navController.navigate(NavRoutes.EntityDetail(type, entityId))
```

`GameApi.fetchEntityList(type, …)` and `fetchEntityDetails(type, id)` just read `type.apiPath`. The `EntityDetailScreen` paginates through `/games?{type.gamesFilterParam}={id}` to show all games linked to the selected entity, reusing the same `GamesListPagingSource` that powers the home feed.

Adding a new catalog endpoint is a single enum entry.

---

## API

Data comes from the public [RAWG API](https://api.rawg.io/docs/). The base URL is configured once in `KtorClientFactory`:

```
https://api.rawg.io/api/
```

Endpoints consumed:

- `GET /games?page=&page_size=` — paginated games list
- `GET /games/{id}` — game details
- `GET /{developers|publishers|genres|platforms|stores}?page=&page_size=` — entity list
- `GET /{developers|publishers|genres|platforms|stores}/{id}` — entity details
- `GET /games?{developers|publishers|genres|platforms|stores}={id}&page=&page_size=` — games filtered by entity

Error handling is centralized in `safeApiCall`, which maps every Ktor, IO, and serialization exception into a typed `AppError` (`NoConnection`, `Timeout`, `Http(code)`, `Serialization`, `Unknown`) wrapped in `UiState.Error`. `AppError.userMessage` owns the user-facing string so UI and logs stay in sync. `CancellationException` is always re-thrown so coroutines remain cooperatively cancellable.

---

## Quality

| Tool | Command | Enforces |
|---|---|---|
| **Spotless (ktlint)** | `./gradlew spotlessCheck` / `spotlessApply` | Kotlin formatting |
| **Detekt** | `./gradlew detekt` | Static analysis (complexity, style, naming, exceptions) |
| **Android Lint** | `./gradlew :app:lintDebug` | Framework + lib linting with a baseline file |
| **Unit tests** | `./gradlew testDebugUnitTest` | Mappers, `safeApiCall`, repository (Ktor MockEngine), view models (Turbine + kotlinx-coroutines-test) |
| **Compose UI tests** | `./gradlew :app:connectedDebugAndroidTest` | Shared Compose components (e.g. `ErrorItem`) |
| **R8 / minification** | Enabled on release with tuned ProGuard rules for kotlinx-serialization, Ktor, coroutines, and Koin |

Every push / PR to `main` runs Spotless, Detekt, unit tests, Android Lint, and `assembleDebug` in [GitHub Actions](.github/workflows/ci.yml) — and uploads the debug APK as a build artifact.

---

## Roadmap

- [ ] Search (RAWG `?search=` parameter)
- [ ] Favorites (local Room persistence)
- [ ] Offline cache for the games feed with a Paging 3 `RemoteMediator`
- [ ] Split into Gradle modules (`:core`, `:data`, `:domain`, `:feature-*`)
- [ ] Baseline Profile generation for startup / frame metrics

---

## License

Released under the MIT License. See [LICENSE](LICENSE) for details.

RAWG content is provided by [RAWG.io](https://rawg.io). Please follow their [API terms](https://rawg.io/apidocs).
