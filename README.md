# Game Radar

A modern Android app for discovering video games, powered by the [RAWG Video Games Database](https://rawg.io/apidocs). Browse a paginated catalog of games, open rich detail pages with ESRB ratings, Metacritic scores, platforms, stores and descriptions, and drill into curated lists of **Platforms**, **Publishers**, **Developers**, **Genres**, and **Stores** to see all the games each one is associated with.

Built end-to-end with **100% Kotlin** and **Jetpack Compose**, following a **Clean Architecture + MVVM** layering and leaning on a modern, fully-coroutine-based stack (Ktor, Koin, Paging 3, Coil).

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

<!-- Add screenshots here once captured: docs/screenshots/home.png, detail.png, drawer.png, entity-list.png, entity-detail.png -->
_Screenshots coming soon._

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

Error handling is centralized in `safeApiCall`, which maps every Ktor exception (`ClientRequestException`, `ServerResponseException`, `HttpRequestTimeoutException`, …) to a `UiState.Error` with a human-readable message, while still propagating `CancellationException`.

---

## Roadmap

- [ ] Search (RAWG `?search=` parameter)
- [ ] Favorites (local Room persistence)
- [ ] Offline cache for the games feed
- [ ] Screenshots gallery on the game detail screen
- [ ] Dark-mode-aware dynamic color (Material You)
- [ ] Unit tests for repositories and use cases
- [ ] Instrumentation tests for the navigation graph

---

## License

Released under the MIT License. See [LICENSE](LICENSE) for details.

RAWG content is provided by [RAWG.io](https://rawg.io). Please follow their [API terms](https://rawg.io/apidocs).
