# Daygame App — Testing Plan

**Project:** `GameApp` (`com.barryburgle.gameapp`)  
**Version:** 1.14.0 · **Room DB:** v8 · **Architecture:** Single Activity + Jetpack Compose + MVI-style events  
**Last updated:** August 2026

---

## 1. Testing Strategy & Scope

### 1.1 Overview

Daygame is a single-module Android app (`:app`) with no Repository or UseCase layer. ViewModels combine Room DAO `Flow`s via custom `CombineN` helpers and expose a single `state: StateFlow`. User actions flow through sealed event interfaces (`GameEvent`, `OutputEvent`, `StatsEvent`, `ToolEvent`) into `ViewModel.onEvent()`.

The testing pyramid for this codebase:

| Layer | Target | Location | Runner |
|-------|--------|----------|--------|
| **Unit** | Pure services, managers, ViewModel logic | `app/src/test/` | JVM (fast, default in CI) |
| **Integration** | Room DAOs, migrations, CSV/data exchange | `app/src/test/` (Room in-memory) + `app/src/androidTest/` (filesystem) | JVM + device/emulator |
| **UI / Compose** | Screens, dialogs, navigation, semantics | `app/src/androidTest/` | Instrumented (emulator/device) |
| **Performance** | Chart rendering, large list scroll, DB aggregation | `app/src/androidTest/` (Macrobenchmark optional) | On-demand / path-triggered |

### 1.2 Current State

**Existing unit tests (11 classes, all JVM):**

| Test class | Package | Coverage |
|------------|---------|----------|
| `AbstractSessionServiceTest` | `service` | Ratio/index math (9 tests) |
| `BatchSessionServiceTest` | `service.batch` | Session init |
| `ChallengeServiceTest` | `service.challenge` | Challenge init |
| `DateServiceTest` | `service.date` | Date init |
| `SetServiceTest` | `service.set` | Set init |
| `EntityServiceTest` | `service` | Time/day/week computation |
| `FormatServiceTest` | `service` | Date/time formatting |
| `GlobalStatsServiceTest` | `service` | 18 stat computations |
| `SessionManagerTest` | `manager` | Aggregation, moving averages, histograms |
| `ServiceTestData` | `service` | Shared fixtures (base class) |
| `ExampleUnitTest` | root | Placeholder |

**Existing instrumented tests:** `ExampleInstrumentedTest` only (package name assertion).

**Gaps:** No ViewModel tests, no Room/DAO tests, no Compose UI tests, no CSV/network tests, no CI pipeline.

### 1.3 Frameworks & Libraries

#### Currently in `app/build.gradle`

```gradle
testImplementation libs.junit                          // JUnit 4.13.2
androidTestImplementation libs.ext.junit             // AndroidX JUnit 1.3.0
androidTestImplementation libs.espresso.core         // Espresso 3.7.0
```

#### Recommended additions (`gradle/libs.versions.toml` + `app/build.gradle`)

| Library | Purpose | Scope |
|---------|---------|-------|
| `androidx.arch.core:core-testing` | `InstantTaskExecutorRule` for LiveData (if needed) | `testImplementation` |
| `org.jetbrains.kotlinx:kotlinx-coroutines-test` | `runTest`, `StandardTestDispatcher`, `UnconfinedTestDispatcher` | `testImplementation` |
| `io.mockk:mockk` | Mock DAOs, `Context`, location client, notification scheduler | `testImplementation` |
| `app.cash.turbine:turbine` | Assert `StateFlow` / `Flow` emissions | `testImplementation` |
| `androidx.lifecycle:lifecycle-runtime-testing` | `ViewModel` + coroutine scope testing | `testImplementation` |
| `androidx.room:room-testing` | In-memory Room DB for DAO/migration tests | `testImplementation` |
| `com.squareup.okhttp3:mockwebserver` | Fake GitHub release API | `testImplementation` |
| `androidx.compose.ui:ui-test-junit4` | Compose UI tests | `androidTestImplementation` |
| `androidx.compose.ui:ui-test-manifest` | Idling for Compose (debug) | `debugImplementation` |
| `androidx.navigation:navigation-testing` | NavHost route assertions | `androidTestImplementation` |
| `com.google.truth:truth` | Readable assertions (optional, replaces `junit.framework.TestCase`) | `testImplementation` |

**Note:** Stay on **JUnit 4** for consistency with existing tests and Android Gradle Plugin defaults. JUnit 5 is not required unless the team wants a migration.

#### Test infrastructure to add

```
app/src/test/java/com/barryburgle/gameapp/
├── CoroutineTestRule.kt          // Main dispatcher override
├── FakeDaos.kt                   // In-memory DAO fakes OR Room in-memory helpers
├── ViewModelTestFactory.kt       // Mirrors MainActivity ViewModelProvider.Factory
└── TestFixtures.kt               // Extends ServiceTestData with Room entities

app/src/androidTest/java/com/barryburgle/gameapp/
├── ComposeTestRule.kt            // GameAppOriginalTheme wrapper
├── FakeViewModelStates.kt        // Pre-baked InputState/OutputState/...
└── NavigationTest.kt
```

---

## 2. Unit Tests (ViewModels & Use Cases)

There is no UseCase layer. Business logic lives in `service/*`, `manager/SessionManager`, and ViewModel `onEvent()` handlers. Service-layer tests already exist; **ViewModels are the highest-priority new unit-test target**.

### 2.1 Coroutine & MockK Guidelines

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

| Dispatcher | When to use |
|------------|-------------|
| `StandardTestDispatcher` | Default. Explicit `advanceUntilIdle()` after `onEvent()` calls that launch coroutines. |
| `UnconfinedTestDispatcher` | Flow collection where you want immediate emission without manual advancement (use sparingly). |

**MockK patterns for this app:**

```kotlin
// DAO returning Flow
every { abstractSessionDao.getAll() } returns flowOf(ServiceTestData().abstractSessionList)

// Suspend insert
coEvery { abstractSessionDao.insert(any()) } returns 1L

// Verify side effects
coVerify { settingDao.insert(Setting(SettingDao.THEME_ID, "urban")) }

// Relaxed mock for Context (InputViewModel)
val context = mockk<Context>(relaxed = true)
```

**InputViewModel Android dependencies to mock/stub:**

- `AndroidNotificationScheduler` — inject via constructor refactor, or mock `context` and use `spyk` on a test subclass
- `FusedLocationProviderClient` — mock; never hit Play Services in unit tests
- `ActivityCompat.checkSelfPermission` — stub to `PERMISSION_GRANTED` or `DENIED`

**Turbine pattern for `state`:**

```kotlin
viewModel.state.test {
    val initial = awaitItem()
    viewModel.onEvent(GameEvent.SetSets("5"))
    advanceUntilIdle()
    val updated = awaitItem()
    assertEquals("5", updated.sets)
    cancelAndIgnoreRemainingEvents()
}
```

---

### 2.2 `InputViewModel`

**Path:** `app/src/main/java/com/barryburgle/gameapp/ui/input/InputViewModel.kt`  
**State:** `InputState` (+ nested `DialogSettingsState`, `ExportSettingsState`, `ShareSettingsState`, `SortTypeState`)  
**Public API:** `val state: StateFlow<InputState>`, `fun onEvent(event: GameEvent)`

**Dependencies to mock:** `AbstractSessionDao`, `SettingDao`, `LeadDao`, `DateDao`, `SetDao`, `ChallengeDao`, `PinPointDao`, `AggregatedSessionsDao`, `AggregatedDatesDao`, `Context`

#### 2.2.1 Initial state & Flow combination

| Test | Assertion |
|------|-----------|
| `state_emitsDefaultInputState_onSubscription` | First emission matches `InputState()` defaults |
| `state_reflectsDaoSessionList` | When `abstractSessionDao.getByDate()` emits list, `state.allSessions` updates |
| `state_reflectsAllEntityLists` | Leads, dates, sets, challenges, pinpoints flow into state |
| `sessionSortType_change_reQueriesDao` | `GameEvent.SortSessions(SETS)` → DAO `getBySets()` subscribed via `flatMapLatest` |
| `dateSortType_change_reQueriesDao` | Each `DateSortType` variant triggers correct DAO method |
| `setSortType_change_reQueriesDao` | Each `SetSortType` variant triggers correct DAO method |
| `challengeSortType_change_reQueriesDao` | Challenge sort switches DAO query |
| `gameEventSortType_mergesEntities` | `SortGameEvents` produces sorted `allEvents` list |

#### 2.2.2 Session CRUD (batch mode)

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `setDate_updatesState` | `SetDate("2024-01-15")` | `state.date == "2024-01-15"` |
| `setStartHour_updatesState` | `SetStartHour("14:30")` | `state.startHour` updated |
| `setEndHour_updatesState` | `SetEndHour("16:00")` | `state.endHour` updated |
| `setSets_updatesState` | `SetSets("10")` | `state.sets == "10"` |
| `setConvos_withFollowCount_incrementsSets` | `SetConvos("3")` when `followCount=true` and convos increased | `sets` incremented by 1 |
| `setContacts_withFollowCount_incrementsSetsAndConvos` | `SetContacts("2")` when `followCount=true` | both counters incremented |
| `saveAbstractSession_insertsSessionAndLeads` | Fill fields → `SaveAbstractSession` | `abstractSessionDao.insert` called; leads inserted with `sessionId` |
| `saveAbstractSession_resetsFormState` | After save | `isAddingSession=false`, fields cleared, `justSaved=true` |
| `saveAbstractSession_updateMode_preservesId` | `isUpdatingSession=true` + `SaveAbstractSession` | Insert with existing `editAbstractSession.id` |
| `editSession_populatesFormFromSession` | `EditSession(session)` | Form fields match session (date/hour substring logic) |
| `deleteSession_deletesPinPointsAndSession` | `DeleteSession(session)` | `pinPointDao.deleteAllBySourceEventIdAndSourceEventType` then `abstractSessionDao.delete` |
| `hideDialog_clearsAllDialogFlags` | `HideDialog` | All `isAdding*` / `isUpdating*` flags false |

#### 2.2.3 Live session

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `setIsAddingLiveSession_setsFlag` | `SetIsAddingLiveSession` | `isAddingLiveSession=true` |
| `setSetsLive_incrementsPinPoint_whenEnabled` | `SetSetsLive(session, 5)` + `pinPointInteractions=true` | PinPoint insert attempted (mock location) |
| `setSetsLive_decrementsPinPoint_onDecrease` | Sets decreased | `pinPointDao.deleteLastPinPointBySourceEventIdAndSourceEventTypeAndType` |
| `setConvosLive_incrementsSets_whenFollowCount` | `SetConvosLive(..., isIncreasing=true)` | Session sets +1 if followCount |
| `setContactsLive_incrementsSetsAndConvos_whenFollowCount` | Contact increase | Both counters cascade |
| `stopLiveSession_finalizesSession` | `StopLiveSession(session)` | End hour set to now; session inserted; sitting reminder cancelled |
| `rollbackAllPinPoints_deletesSessionPinPoints` | `RollbackAllPinPoints(session)` | All session pinpoints deleted |
| `rollbackContactPinPointForLeadInsertDismissal` | Event with `sessionId` | Last contact pinpoint deleted |
| `deletePinPoint_callsDaoDelete` | `DeletePinPoint(pinPoint)` | `pinPointDao.delete` invoked |

#### 2.2.4 Lead management

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `showLeadDialog_setsFlags` | `ShowLeadDialog(addLead=true, ...)` | Dialog visibility flags |
| `hideLeadDialog_clearsFlags` | `HideLeadDialog` | Lead dialog flags reset |
| `setLeadName_setLeadContact_setLeadAge_updateState` | Individual setters | Corresponding state fields |
| `saveLead_insertsViaDao_whenUpdatingLead` | `SaveLead(lead)` + `isUpdatingLead=true` | `leadDao.insert` |
| `deleteLead_filtersLocalList_whenNotUpdating` | `DeleteLead(lead)` + `isUpdatingLead=false` | Lead removed from `state.leads` |
| `deleteLead_callsDao_whenUpdating` | `DeleteLead` + `isUpdatingLead=true` | `leadDao.delete` |
| `editLead_populatesForm` | `EditLead(lead, isUpdatingLead=true/false)` | Correct fields populated |
| `emptyLeads_clearsLeadList` | `EmptyLeads` | `leads` empty |
| `switchSaveLeadToLiveSession_togglesFlag` | `SwitchSaveLeadToLiveSession` | Flag toggled |

#### 2.2.5 Date CRUD

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `setLeadId_setLocation_setCost_updateState` | Field setters | State updated |
| `switchPull_switchBounce_switchKiss_switchLay_toggleBooleans` | Toggle events | Nullable booleans flip |
| `saveDate_insertsViaDateService` | Fill + `SaveDate` | `dateDao.insert` with computed fields |
| `editDate_populatesForm` | `EditDate(date)` | Form matches entity |
| `deleteDate_callsDaoDelete` | `DeleteDate(date)` | `dateDao.delete` |
| `sortDates_switchesQuery` | Each `DateSortType` | Correct DAO sort method |

#### 2.2.6 Set CRUD

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `saveSet_insertsViaSetService` | `SaveSet` | `setDao.insert`; optional auto-iDate when `generateiDate=true` |
| `editSet_populatesForm` | `EditSet(set)` | Form fields match |
| `deleteSet_callsDaoDelete` | `DeleteSet(set)` | `setDao.delete` |
| `switchConversation_switchContact_switchInstantDate` | Toggle events | Boolean flags updated |
| `sortSets_switchesQuery` | Each `SetSortType` | Correct DAO method |

#### 2.2.7 Challenge CRUD

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `setChallengeName_through_setChallengeGoal_updateState` | Field setters | Challenge form fields |
| `saveChallenge_insertsViaChallengeService` | `SaveChallenge` | `challengeDao.insert` |
| `editChallenge_populatesForm` | `EditChallenge(challenge)` | Form populated |
| `deleteChallenge_callsDaoDelete` | `DeleteChallenge(challenge)` | `challengeDao.delete` |
| `sortChallenges_switchesQuery` | Each `ChallengeSortType` | Correct DAO method |

#### 2.2.8 Notifications & overlay

| Test | Event(s) | Assertion |
|------|----------|-----------|
| `scheduleLiveSessionSittingReminder_callsScheduler` | `ScheduleLiveSessionSittingReminder(30)` | Scheduler called with correct request code |
| `schedulePullOClockReminder_callsScheduler` | `SchedulePullOClockReminder(60)` | Scheduler invoked |
| `scheduleWriteHerAfterReminder_includesLeadInfo` | Event with lead desc/link | Notification payload correct |
| `setIsInOverlayToTrue/False` | Overlay events | `isInOverlay` flag toggled |
| `switchShowFlag_togglesVisibilityFlags` | `SwitchShowFlag(n)` | Correct show flag inverted |
| `switchJustSaved_resetsJustSaved` | `SwitchJustSaved` | `justSaved=false` |

---

### 2.3 `OutputViewModel`

**Path:** `app/src/main/java/com/barryburgle/gameapp/ui/output/OutputViewModel.kt`  
**State:** `OutputState`  
**Public API:** `val state: StateFlow<OutputState>`, `fun onEvent(event: OutputEvent)`

**Dependencies to mock:** `AbstractSessionDao`, `AggregatedSessionsDao`, `AggregatedDatesDao`, `SettingDao`, `LeadDao`, `DateDao`, `SetDao`

#### 2.3.1 State combination

| Test | Assertion |
|------|-----------|
| `state_emitsCombinedData_fromAllDaos` | Sessions (limited + unlimited), leads, dates, sets, weekly/monthly aggregates present |
| `state_normalizesSessionIds` | `SessionManager.normalizeSessionsIds` applied to session lists |
| `state_reflectsMovingAverageWindow` | `settingDao.getAverageLast()` value in `movingAverageWindow` |

#### 2.3.2 `onEvent` handlers

| Test | Event | Assertion |
|------|-------|-----------|
| `switchShowLeadLegend_togglesFlag` | `SwitchShowLeadLegend` | `showLeadsLegend` inverted |
| `switchShowIndexFormula_togglesFlag` | `SwitchShowIndexFormula` | `showIndexFormula` inverted |
| `switchShowCustomSummaryDialog_togglesFlag` | `SwitchShowCustomSummaryDialog` | `showCustomSummaryDialog` inverted |

---

### 2.4 `StatsViewModel`

**Path:** `app/src/main/java/com/barryburgle/gameapp/ui/stats/StatsViewModel.kt`  
**State:** `StatsState`  
**Public API:** `val state: StateFlow<StatsState>`, `fun onEvent(event: StatsEvent)`

**Dependencies to mock:** `AbstractSessionDao`, `LeadDao`, `DateDao`, `ChallengeDao`, `SetDao`, `PinPointDao`, `SettingDao`

#### 2.4.1 State combination

| Test | Assertion |
|------|-----------|
| `state_emitsAllHistograms` | Sets/convos/contacts histograms from session DAO; age/nationality from lead/date DAOs |
| `state_filtersMapPinPoints_bySelectedTypes` | `SelectMapPinPointType` filters `typeFilteredMapPinPoints` |
| `state_filtersTimePinPoints_bySelectedTypes` | `SelectTimePinPointType` filters `typeFilteredTimePinPoints` |
| `state_reflectsCopyReportOnClipboardSetting` | Boolean from `settingDao.getCopyReportOnClipboard()` |

#### 2.4.2 `onEvent` handlers

| Test | Event | Assertion |
|------|-------|-----------|
| `showInfo_setsDialogStateAndHistogramType` | `ShowInfo(SESSION_SETS)` | `isShowingInfo=true`, title/entity from enum, `_loadInfoType` switches histogram |
| `showInfo_eachStatsLoadInfoEnum` | All 8 `StatsLoadInfoEnum` values | Correct histogram DAO subscribed |
| `hideInfo_closesDialog` | `HideInfo` | `isShowingInfo=false` |
| `selectMapPinPointType_updatesSelectionList` | `SelectMapPinPointType(list)` | `mapPinPointsTypeSelectionList` updated |
| `selectTimePinPointType_updatesSelectionList` | `SelectTimePinPointType(list)` | `timePinPointsTypeSelectionList` updated |

---

### 2.5 `ToolViewModel`

**Path:** `app/src/main/java/com/barryburgle/gameapp/ui/tool/ToolViewModel.kt`  
**State:** `ToolsState` (+ `ImportExportSettingState`, `GeneralSettingState`, `LiveSessionSettingState`)  
**Public API:** `val state: StateFlow<ToolsState>`, `fun onEvent(event: ToolEvent)`, `fun minMaxLimiter(count, min, max): Int`

**Dependencies to mock:** All 7 DAOs

#### 2.5.1 `minMaxLimiter` (pure function — test without mocks)

| Test | Input | Expected |
|------|-------|----------|
| `minMaxLimiter_returnsCount_whenInRange` | `(5, 1, 10)` | `5` |
| `minMaxLimiter_clampsToMin` | `(0, 1, 10)` | `1` |
| `minMaxLimiter_clampsToMax` | `(15, 1, 10)` | `10` |

#### 2.5.2 Import/export filename settings

For each `ToolEvent.SetExport*FileName` / `SetImport*FileName` (sessions, leads, dates, sets, challenges, pinpoints, settings):

| Test | Assertion |
|------|-----------|
| `setExportSessionsFileName_updatesStateAndPersists` | State field updated + `settingDao.insert` with correct `SettingDao.*_ID` |
| *(repeat for all 14 filename events)* | |

#### 2.5.3 Folder & batch settings

| Test | Event | Assertion |
|------|-------|-----------|
| `setExportFolder_persistsSetting` | `SetExportFolder` | `SettingDao.EXPORT_FOLDER_ID` |
| `setImportFolder_persistsSetting` | `SetImportFolder` | `SettingDao.IMPORT_FOLDER_ID` |
| `setBackupFolder_persistsSetting` | `SetBackupFolder` | `SettingDao.BACKUP_FOLDER_ID` |
| `setLastSessionAverageQuantity_clampedAndPersisted` | `SetLastSessionAverageQuantity` | Value clamped via `minMaxLimiter`, persisted |
| `setLastSessionsShown_clampedAndPersisted` | `SetLastSessionsShown` | Clamped 1–20 |
| `setLastWeeksShown_clampedAndPersisted` | `SetLastWeeksShown` | Clamped 1–20 |
| `setLastMonthsShown_clampedAndPersisted` | `SetLastMonthsShown` | Clamped 1–20 |
| `setNotificationTime_persisted` | `SetNotificationTime` | Setting inserted |
| `setExportHeader_setImportHeader_persisted` | Boolean events | Correct setting IDs |

#### 2.5.4 Toggle switches (persist to `SettingDao`)

| Test | Event | Assertion |
|------|-------|-----------|
| `switchBackupActive_togglesAndPersists` | `SwitchBackupActive` | Flag inverted + setting saved |
| `switchGenerateiDate_togglesAndPersists` | `SwitchGenerateiDate` | |
| `switchPinPointInteractions_togglesAndPersists` | `SwitchPinPointInteractions` | |
| `switchFollowCount_togglesAndPersists` | `SwitchFollowCount` | |
| `switchThemeSysFollow_togglesAndPersists` | `SwitchThemeSysFollow` | |
| `switchNeverShareLeadInfo_togglesAndPersists` | `SwitchNeverShareLeadInfo` | |
| `switchCopyReportOnClipboard_togglesAndPersists` | `SwitchCopyReportOnClipboard` | |
| `switchShowCurrentWeekSummary_togglesAndPersists` | `SwitchShowCurrentWeekSummary` | |
| `switchShowCurrentMonthSummary_togglesAndPersists` | `SwitchShowCurrentMonthSummary` | |
| `switchShowCurrentChallengeSummary_togglesAndPersists` | `SwitchShowCurrentChallengeSummary` | |
| `switchArchiveBackupFolder_togglesAndPersists` | `SwitchArchiveBackupFolder` | |
| `switchLiveSessionNotification_togglesAndPersists` | `SwitchLiveSessionNotification` | |
| `switchLiveSessionSittingReminder_togglesAndPersists` | `SwitchLiveSessionSittingReminder` | |
| `switchWriteHerReminder_togglesAndPersists` | `SwitchWriteHerReminder` | |
| `switchLiveSessionShare_togglesAndPersists` | `SwitchLiveSessionShare` | |
| `switchShowChangelog_togglesState` | `SwitchShowChangelog` | UI flag only |
| `switchBackupBeforeUpdate_togglesAndPersists` | `SwitchBackupBeforeUpdate` | |
| `switchSuggestLeadsNationality_togglesAndPersists` | `SwitchSuggestLeadsNationality` | |
| `switchSimplePlusOneReport_togglesAndPersists` | `SwitchSimplePlusOneReport` | |
| `switchIsCleaning_togglesAndPersists` | `SwitchIsCleaning` | |

#### 2.5.5 Delete-all operations

| Test | Event | Assertion |
|------|-------|-----------|
| `deleteAllSessions_callsDaoDeleteAll` | `DeleteAllSessions` | `abstractSessionDao.deleteAll()` |
| `deleteAllLeads_callsDaoDeleteAll` | `DeleteAllLeads` | `leadDao.deleteAll()` |
| `deleteAllDates_callsDaoDeleteAll` | `DeleteAllDates` | `dateDao.deleteAll()` |
| `deleteAllSets_callsDaoDeleteAll` | `DeleteAllSets` | `setDao.deleteAll()` |
| `deleteAllChallenges_callsDaoDeleteAll` | `DeleteAllChallenges` | `challengeDao.deleteAll()` |
| `deleteAllPinPoints_callsDaoDeleteAll` | `DeleteAllPinPoints` | `pinPointDao.deleteAll()` |
| `deleteAllSettings_callsDaoDeleteAll` | `DeleteAllSettings` | `settingDao.deleteAll()` |

#### 2.5.6 Theme & challenge settings

| Test | Event | Assertion |
|------|-------|-----------|
| `setTheme_persistsThemeId` | `SetTheme("urban")` | `SettingDao.THEME_ID` |
| `setIncrementChallengeGoal_clampedAndPersisted` | `SetIncrementChallengeGoal` | Clamped + persisted |
| `setDefaultChallengeGoal_clampedAndPersisted` | `SetDefaultChallengeGoal` | Clamped + persisted |
| `setDeleteConfirmationPrompt_persisted` | `SetDeleteConfirmationPrompt` | Setting saved |
| `setLiveSessionSittingReminderInterval_persisted` | `SetLiveSessionSittingReminderInterval` | |
| `setWriteHerReminderInterval_persisted` | `SetWriteHerReminderInterval` | |
| `setPullOClockReminderInterval_persisted` | `SetPullOClockReminderInterval` | |

#### 2.5.7 Batch entity import (in-memory state only)

| Test | Event | Assertion |
|------|-------|-----------|
| `setAllSessions_updatesStateList` | `SetAllSessions(list)` | `state.allSessions` replaced |
| `setAllLeads_updatesStateList` | `SetAllLeads(list)` | |
| `setAllDates_updatesStateList` | `SetAllDates(list)` | |
| `setAllSets_updatesStateList` | `SetAllSets(list)` | |
| `setAllChallenges_updatesStateList` | `SetAllChallenges(list)` | |
| `setAllPinPoints_updatesStateList` | `SetAllPinPoints(list)` | |
| `setAllSettings_updatesStateList` | `SetAllSettings(list)` | |

---

### 2.6 Service Layer (extend existing coverage)

These classes have **no ViewModel** but contain critical business logic. Extend the existing `ServiceTestData`-based tests:

| Class | Package | Priority tests to add |
|-------|---------|----------------------|
| `DataExchangeService` | `service.exchange` | `backup`, `export`, `import`, `exportAll`, `importAll`, round-trip data integrity |
| `AbstractCsvService` | `service.csv` | Row validation, header handling, malformed CSV |
| `SessionCsvService` | `service.csv` | Export/import round-trip for sessions |
| `LeadCsvService` | `service.csv` | Same |
| `DateCsvService` | `service.csv` | Same |
| `SetCsvService` | `service.csv` | Same |
| `ChallengeCsvService` | `service.csv` | Same |
| `PinPointCsvService` | `service.csv` | Same |
| `SettingCsvService` | `service.csv` | Same |
| `CSVFindService` | `service.csv` | `findCsvFiles`, `archiveBackups`, `getLastFilenameInFolder` |
| `PhoneBookService` | `service` | `findSimilarContact`, `levenshteinDistance` edge cases |
| `ChallengeMedalService` | `service.challenge` | `getMedals`, `getMedal` for each `ChallengeMedalEnum` |

---

## 3. UI / Jetpack Compose Tests

### 3.1 Prerequisites

1. Add `ui-test-junit4` and `ui-test-manifest` dependencies.
2. Add **`Modifier.testTag(...)`** to interactive elements (currently none exist). Recommended tags:

| Screen / Component | Suggested testTag |
|------------------|-------------------|
| Bottom nav items | `nav_game`, `nav_dashboard`, `nav_results`, `nav_settings` |
| `InputScreen` FAB | `input_fab_add` |
| `EventCard` delete button | `event_card_delete_{id}` |
| `SessionDialog` save button | `session_dialog_save` |
| `LeadDialog` save button | `lead_dialog_save` |
| `InputCounter` +/- buttons | `counter_plus_{field}`, `counter_minus_{field}` |
| `ToolsScreen` delete-all confirm | `delete_all_confirm` |
| `HeatmapCalendar` | `heatmap_calendar` |
| `OutputBarChart` / `OutputLineChart` | `chart_bar`, `chart_line` |
| `PinPointScatterChart` | `chart_scatter` |

3. Wrap tests in `GameAppOriginalTheme { ... }` for consistent theming.

### 3.2 Key screens requiring UI tests

| Composable | Package | Route | Complexity | Priority |
|------------|---------|-------|------------|----------|
| `Navigation` | `ui.navigation` | — | Bottom nav + NavHost | P0 |
| `InputScreen` | `ui.input` | `sessions` | Lists, FAB, live session, sorting | P0 |
| `EventCard` + bodies | `ui.input.card` | — | Expand/collapse, delete confirm | P0 |
| `SessionDialog` | `ui.input.dialog` | — | Form fields, counters, save | P0 |
| `LeadDialog` | `ui.input` | — | Lead form, nationality search | P1 |
| `SetDialog`, `DateDialog`, `ChallengeDialog` | `ui.input.dialog` | — | Entity-specific forms | P1 |
| `InputCounter` / `InputCountComponent` | `ui.input` | — | Increment/decrement | P0 |
| `OutputScreen` | `ui.output` | `dashboard` | Charts, heatmap, sections | P1 |
| `HeatmapCalendar` | `ui.output` | — | Day cells, color mapping | P2 |
| `CustomSummaryDialog` | `ui.output.dialog` | — | Summary display toggle | P2 |
| `StatsScreen` | `ui.stats` | `stats` | Histograms, scatter map | P1 |
| `InfoDialog` | `ui.stats.dialog` | — | Info overlay | P2 |
| `PinPointScatterCard` | `ui.stats` | — | Map/chart interaction | P2 |
| `ToolsScreen` | `ui.tool` | `settings` | Settings cards, toggles | P1 |
| `DeleteDialog` | `ui.tool.dialog` | — | Destructive confirmation | P0 |
| `DataExchangeCard` / `BackupCard` | `ui.tool` | — | Import/export triggers | P2 |
| `ThemeCard` | `ui.tool` | — | Theme selector | P2 |

### 3.3 ComposeTestRule criteria

```kotlin
@RunWith(AndroidJUnit4::class)
class InputScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun inputScreen_displaysSessionList() {
        composeTestRule.setContent {
            GameAppOriginalTheme {
                InputScreen(state = fakeInputState, onEvent = {})
            }
        }
        composeTestRule.onNodeWithTag("input_fab_add").assertIsDisplayed()
        composeTestRule.onNodeWithText("3 sets").assertIsDisplayed()
    }
}
```

**Assertion categories:**

| Category | APIs | Example |
|----------|------|---------|
| Display | `assertIsDisplayed()`, `assertDoesNotExist()` | FAB visible on Game tab |
| Interaction | `performClick()`, `performScrollTo()`, `performTextInput()` | Tap counter + button |
| State change | Recomposition with updated state lambda | Dialog opens when `isAddingSession=true` |
| Navigation | `onNodeWithTag("nav_dashboard").performClick()` | Route switches to dashboard |
| Semantics | `onNodeWithContentDescription()`, `assertIsToggleable()` | Switch settings in ToolsScreen |
| Accessibility | `assertHasClickAction()`, custom semantics | All buttons have labels |

### 3.4 Isolation strategy

| Approach | Use for | How |
|----------|---------|-----|
| **State injection (preferred)** | Screen composables | Pass fake `InputState`/`OutputState`/… and capture events in `mutableListOf<Event>()` |
| **Preview-based screenshot tests** | Visual regression (optional) | `@Preview` composables in `androidTest` with Paparazzi or Compose preview tests |
| **Full instrumented E2E** | Critical user journeys | Real `MainActivity` + in-memory Room DB on emulator |
| **Robolectric + Compose** | Not recommended initially | Project has no Robolectric; instrumented tests are simpler for maps/charts |

#### Recommended E2E journeys (instrumented)

1. **Create session flow:** Game tab → FAB → fill `SessionDialog` → save → card appears in list
2. **Live session flow:** Start live session → increment sets/convos/contacts → stop session
3. **Navigation smoke:** All 4 bottom tabs render without crash
4. **Delete-all guard:** Settings → Delete → confirmation dialog → cancel (no data loss)
5. **Theme switch:** Settings → ThemeCard → select palette → theme applies

### 3.5 Custom composables requiring focused UI tests

| Composable | File | Tests |
|------------|------|-------|
| `InputCounter` | `InputCounter.kt` | Plus/minus bounds, display value |
| `EntitySorter` | `EntitySorter.kt` | Sort button opens options, selection callback |
| `GenericShadowButton` | `ui.utilities` | Click ripple, enabled/disabled |
| `SwitchSetting` | `ui.utilities.setting` | Toggle state, label display |
| `Timeline` | `ui.utilities` | Event ordering display |
| `ScrollableSelector` | `ui.tool` | Horizontal scroll selection |
| `OutputBarChart` | `chart/OutputBarChart.kt` | Renders with empty/non-empty data (no crash) |
| `PinPointScatterChart` | `chart/PinPointScatterChart.kt` | Renders filtered pinpoints |

---

## 4. Integration & Repository Tests

There is no Repository layer. Integration tests target **Room DAOs**, **migrations**, **CSV services**, and **network API**.

### 4.1 Room in-memory database tests

**Setup:**

```kotlin
@RunWith(AndroidJUnit4::class) // or JUnit4 for JVM with Robolectric; prefer androidTest for Room 2.8+
class AbstractSessionDaoTest {
    private lateinit var db: GameAppDatabase
    private lateinit var dao: AbstractSessionDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GameAppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.abstractSessionDao
    }

    @After
    fun closeDb() {
        db.close()
        GameAppDatabase.destroyInstance()
    }
}
```

**Note:** Schema exports exist at `app/schemas/com.barryburgle.gameapp.database.GameAppDatabase/{1,3,4,5,6,7,8}.json` — use `MigrationTestHelper` for migration tests.

#### DAO test matrix

| DAO | Package | Critical queries to test |
|-----|---------|--------------------------|
| `AbstractSessionDao` | `dao.session` | `insert`, `getAll`, `getAllLimit`, all 12 sort methods, histograms, `getLastSession`, `getLastLiveSession`, `batchInsert`, cascade delete |
| `AggregatedSessionsDao` | `dao.session` | `groupStatsByWeekNumber`, `groupStatsByMonth` |
| `AggregatedDatesDao` | `dao.date` | Same aggregation queries |
| `LeadDao` | `dao.lead` | CRUD, `getAgeHistogram`, `getNationalityHistogram` |
| `DateDao` | `dao.date` | CRUD, 20+ sort/filter queries, histograms |
| `SetDao` | `dao.set` | CRUD, 15+ sort/filter queries |
| `ChallengeDao` | `dao.challenge` | CRUD, sort queries, `AchievedChallenge` mapping |
| `PinPointDao` | `dao.pinpoint` | CRUD, geo queries, `deleteAllBySourceEventIdAndSourceEventType`, `deleteLastPinPointBySourceEventIdAndSourceEventTypeAndType` |
| `SettingDao` | `dao.setting` | CRUD, all ~40 `get*()` Flow accessors return defaults |

#### Migration tests

| Test | Path |
|------|------|
| `migrateAllVersions_1_to_8` | Apply `MIGRATION_1_2` through `MIGRATION_7_8`; verify tables/columns |
| `migrate7to8_createsPinPointTable` | PinPoint table + `lead.pinpoint_id` column |
| `migrate6to7_addsLeadContactFields` | `contact_lookup_key`, `instagram_url` |

### 4.2 CSV & data exchange integration

Run on JVM with temporary directories (`@TempDir`):

| Test class | Scenarios |
|------------|-----------|
| `SessionCsvServiceIntegrationTest` | Export sessions → import → compare entity fields |
| `DataExchangeServiceIntegrationTest` | Full `exportAll` / `importAll` round-trip |
| `CSVFindServiceIntegrationTest` | Backup archiving, filename discovery |
| `DataExchangeServiceBackupTest` | `backupAllAndClean` preserves data integrity |

Use fixtures from `ServiceTestData` for seed entities.

### 4.3 Network layer

| Test | Setup |
|------|-------|
| `GithubServiceTest` | MockWebServer returning sample `GithubLatestResponse` JSON |
| `RetrofitInstanceTest` | Verify base URL `https://api.github.com`, Gson deserialization |

### 4.4 Notification & background components (instrumented)

| Component | Package | Test |
|-----------|---------|------|
| `AndroidNotificationScheduler` | `notification` | Alarm scheduled with correct `PendingIntent` flags |
| `NotificationReceiver` | `notification` | Intent handling |
| `BootCompletedReceiver` | `notification` | Reschedules on boot |
| `PersistentNotificationService` | `service.notification` | Foreground service starts/stops with live session |
| `DatabaseWorker` | `worker` | HandlerThread processes DB task |

---

## 5. Execution & CI/CD Recommendations

### 5.1 Local Gradle commands

```bash
# All JVM unit tests (fast — run on every change)
./gradlew :app:testDebugUnitTest

# Unit tests with HTML report
./gradlew :app:testDebugUnitTest
# Report: app/build/reports/tests/testDebugUnitTest/index.html

# Single test class
./gradlew :app:testDebugUnitTest --tests "com.barryburgle.gameapp.service.AbstractSessionServiceTest"

# Single test method
./gradlew :app:testDebugUnitTest --tests "com.barryburgle.gameapp.manager.SessionManagerTest.computeMovingAverageTest"

# All instrumented tests (requires connected emulator/device)
./gradlew :app:connectedDebugAndroidTest

# Single instrumented class
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.barryburgle.gameapp.InputScreenTest

# Assemble without tests (CI build verification)
./gradlew :app:assembleDebug
```

**Windows (PowerShell):** Use `.\gradlew.bat` instead of `./gradlew`.

### 5.2 Keeping tests fast

| Practice | Detail |
|----------|--------|
| Default to JVM unit tests | Target < 30s for full `testDebugUnitTest` suite |
| Mock DAOs in ViewModel tests | Never spin up Room unless testing DAO itself |
| Room in-memory for DAO tests only | Close DB in `@After`; parallelize test classes |
| Avoid `Thread.sleep` in Compose tests | Use `composeTestRule.waitForIdle()` and idling resources |
| Disable animations on emulator | `adb shell settings put global animator_duration_scale 0` |
| Split instrumented suites | `@SmallTest` for smoke, `@LargeTest` for E2E |
| Cache Gradle in CI | `actions/cache` for `~/.gradle/caches` |

### 5.3 Recommended GitHub Actions workflow

No CI exists today. Add `.github/workflows/test.yml`:

```yaml
name: Test

on:
  push:
    branches: [main, master]
  pull_request:

jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew :app:testDebugUnitTest --no-daemon

  instrumented-tests:
    runs-on: ubuntu-latest
    if: github.event_name == 'pull_request'
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - uses: gradle/actions/setup-gradle@v4
      - name: Run instrumented tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 34
          script: ./gradlew :app:connectedDebugAndroidTest --no-daemon
```

**Suggested CI tiers:**

| Trigger | Tests run |
|---------|-----------|
| Every PR | `testDebugUnitTest` (required) |
| PR with `ui/` changes | + smoke Compose tests (`NavigationTest`, `InputScreenTest`) |
| PR with `database/` or `dao/` changes | + Room migration tests |
| Nightly / release branch | Full `connectedDebugAndroidTest` + performance suite |
| Manual `workflow_dispatch` | Performance profiling suite |

### 5.4 Test coverage goals (incremental)

| Phase | Target |
|-------|--------|
| Phase 1 | All service/manager tests green; add ViewModel tests for `OutputViewModel`, `StatsViewModel`, `ToolViewModel.minMaxLimiter` |
| Phase 2 | `InputViewModel` core CRUD + live session; Room DAO tests for `AbstractSessionDao`, `SettingDao` |
| Phase 3 | Compose smoke tests + navigation; CSV round-trip |
| Phase 4 | Full `InputViewModel` event coverage; instrumented E2E; performance baseline |

---

## 6. Performance Profiling Testing

### 6.1 What to monitor

Daygame is data-heavy (lists, charts, maps, aggregations). These areas are most likely to become clunky:

| Area | Components | Risk |
|------|------------|------|
| **Game tab list** | `InputScreen`, `EventCard`, `EventFastScroller`, `EntitySorter` | Slow scroll with 500+ mixed events |
| **Dashboard charts** | `OutputBarChart`, `OutputLineChart`, `OutputPieChart`, `SessionManager.computeMovingAverage` | Recomposition + MPAndroidChart bind on large datasets |
| **Heatmap** | `HeatmapCalendar` | Cell rendering for 365+ days |
| **Stats histograms** | `SessionsHistogramsSection`, `LeadsHistogramsSection`, `DatesHistogramsSection` | Multiple charts on one scrollable screen |
| **PinPoint map** | `PinPointScatterChart`, osmdroid | Map tile load + 1000+ markers |
| **DB aggregation** | `AggregatedSessionsDao`, `AggregatedDatesDao`, histogram queries | Query time on large tables |
| **CSV import/export** | `DataExchangeService`, `AbstractCsvService` | Memory/time on full backup |
| **Flow combining** | `CombineSixteen`, `CombineNineteen`, `CombineTwenty` in ViewModels | Excessive recomputation when many DAO flows emit |
| **Live session** | `LiveSessionBody`, `InputCounter`, pinpoint GPS writes | Rapid counter taps + location callbacks |

### 6.2 How to run performance tests

#### A. Macrobenchmark (recommended for scroll/chart jank)

Add `:benchmark` module with `androidx.benchmark:benchmark-macro-junit4`:

```kotlin
@Test
fun scrollInputScreenList() = benchmarkRule.measureRepeated {
    device.findObject(By.res("input_event_list")).fling(Direction.DOWN)
}
```

#### B. Android Studio Profiler (manual)

- **CPU Profiler** during fast scroll on Game tab with 500+ seeded sessions
- **Memory Profiler** during `DataExchangeService.exportAll`
- **System Trace** for `InputViewModel` rapid live counter updates

#### C. Custom timing tests (instrumented)

```kotlin
@Test
fun aggregatedSessionsQuery_completesUnder500ms() {
    seedDatabase(sessionCount = 5000)
    val start = System.nanoTime()
    runBlocking { db.aggregatedSessionsDao.groupStatsByWeekNumber().first() }
    val elapsedMs = (System.nanoTime() - start) / 1_000_000
    assertTrue("Aggregation took ${elapsedMs}ms", elapsedMs < 500)
}
```

#### D. Baseline profiles (release builds)

Generate baseline profiles for `InputScreen` and `OutputScreen` startup to improve production performance.

### 6.3 When to run performance tests (path-triggered)

Run the performance suite **only when relevant source paths change** — not on every PR.

| Changed paths | Performance tests to run |
|---------------|--------------------------|
| `app/src/main/java/com/barryburgle/gameapp/ui/input/**` | Input list scroll benchmark; live counter stress test; `InputViewModel` flow combine profiling |
| `app/src/main/java/com/barryburgle/gameapp/ui/output/**` | Chart render benchmark; heatmap draw test; dashboard scroll |
| `app/src/main/java/com/barryburgle/gameapp/ui/output/chart/**` | MPAndroidChart bind/rebind timing with 100/500/1000 data points |
| `app/src/main/java/com/barryburgle/gameapp/ui/stats/**` | Stats screen scroll; histogram render; `PinPointScatterChart` marker load |
| `app/src/main/java/com/barryburgle/gameapp/ui/stats/chart/**` | Scatter chart with 1000 pinpoints |
| `app/src/main/java/com/barryburgle/gameapp/dao/**` | DAO query timing tests (aggregation, histograms, sort queries) |
| `app/src/main/java/com/barryburgle/gameapp/database/**` | Migration performance; cold-start DB open timing |
| `app/src/main/java/com/barryburgle/gameapp/manager/SessionManager.kt` | Moving average + histogram computation benchmarks |
| `app/src/main/java/com/barryburgle/gameapp/service/exchange/**` | CSV export/import timing with 10k rows |
| `app/src/main/java/com/barryburgle/gameapp/service/csv/**` | Per-entity CSV parse throughput |
| `app/src/main/java/com/barryburgle/gameapp/ui/Combine*.kt` | ViewModel state combine emission count under rapid DAO updates |
| `app/src/main/java/com/barryburgle/gameapp/ui/navigation/**` | Tab switch latency |
| `app/src/main/java/com/barryburgle/gameapp/service/notification/**` | Foreground service start/stop timing |

**Do NOT run performance tests when only these change:**

- `ui/theme/**` (palette/color changes)
- `ui/tool/ThemeCard.kt`, `CreditsCard.kt`, `ShareCard.kt` (non-data UI)
- `model/enums/**` (unless query logic changes)
- String/copy changes in dialogs
- Version bumps in `build.gradle`

### 6.4 Performance thresholds (initial baselines)

Establish baselines on a reference device (e.g., Pixel 6 emulator API 34), then fail CI if exceeded by > 20%:

| Metric | Baseline target |
|--------|-----------------|
| Game tab list scroll (1000 items) | < 16ms/frame (60fps) |
| Dashboard bar chart bind (365 sessions) | < 200ms |
| Stats screen first render | < 500ms |
| `groupStatsByWeekNumber` (5000 sessions) | < 500ms |
| Full CSV export (all entities, 1000 rows each) | < 5s |
| Tab navigation switch | < 100ms perceived |
| Cold start to Game tab interactive | < 2s |

Store baseline results in `app/benchmark/baseline-prof.txt` and track regressions in CI artifacts.

### 6.5 CI integration for performance

```yaml
performance-tests:
  runs-on: ubuntu-latest
  if: |
    contains(github.event.pull_request.changed_files, 'ui/input') ||
    contains(github.event.pull_request.changed_files, 'ui/output') ||
    contains(github.event.pull_request.changed_files, 'dao/')
  steps:
    - uses: reactivecircus/android-emulator-runner@v2
      with:
        api-level: 34
        script: ./gradlew :benchmark:connectedCheck --no-daemon
```

Use `dorny/paths-filter@v3` for reliable path detection:

```yaml
- uses: dorny/paths-filter@v3
  id: changes
  with:
    filters: |
      perf_input:
        - 'app/src/main/java/com/barryburgle/gameapp/ui/input/**'
      perf_charts:
        - 'app/src/main/java/com/barryburgle/gameapp/ui/output/chart/**'
        - 'app/src/main/java/com/barryburgle/gameapp/ui/stats/chart/**'
      perf_data:
        - 'app/src/main/java/com/barryburgle/gameapp/dao/**'
        - 'app/src/main/java/com/barryburgle/gameapp/database/**'
```

---

## Appendix A: File Reference Map

| Layer | Key paths |
|-------|-----------|
| Entry | `MainActivity.kt` |
| Navigation | `ui/navigation/Screen.kt`, `Navigation.kt` |
| ViewModels | `ui/input/InputViewModel.kt`, `ui/output/OutputViewModel.kt`, `ui/stats/StatsViewModel.kt`, `ui/tool/ToolViewModel.kt` |
| Events | `event/GameEvent.kt`, `OutputEvent.kt`, `StatsEvent.kt`, `ToolEvent.kt` |
| State | `ui/input/state/InputState.kt`, `ui/output/state/OutputState.kt`, `ui/stats/state/StatsState.kt`, `ui/tool/state/ToolsState.kt` |
| Database | `database/GameAppDatabase.kt` (v8) |
| DAOs | `dao/session/`, `dao/date/`, `dao/lead/`, `dao/set/`, `dao/challenge/`, `dao/pinpoint/`, `dao/setting/` |
| Services | `service/`, `service/csv/`, `service/exchange/`, `service/batch/`, `service/date/`, `service/set/`, `service/challenge/` |
| Manager | `manager/SessionManager.kt` |
| Tests | `app/src/test/`, `app/src/androidTest/` |
| Schemas | `app/schemas/com.barryburgle.gameapp.database.GameAppDatabase/` |

## Appendix B: Implementation Priority

1. Add test dependencies (MockK, Turbine, coroutines-test, room-testing, compose ui-test)
2. Create `MainDispatcherRule` and `ViewModelTestFactory`
3. Unit test `OutputViewModel`, `StatsViewModel`, `ToolViewModel.minMaxLimiter` (quick wins)
4. Unit test `InputViewModel` session CRUD + live session paths
5. Room in-memory tests for `AbstractSessionDao` + migration 7→8
6. Add `testTag`s to P0 composables; write `NavigationTest` + `InputScreenTest`
7. CSV round-trip integration tests
8. GitHub Actions unit test workflow
9. Performance benchmark module + path-filtered CI job
