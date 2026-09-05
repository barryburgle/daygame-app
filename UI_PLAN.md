# Daygame App — UI/UX Architecture & Scalability Plan

**Project:** `GameApp` (`com.barryburgle.gameapp`)  
**UI package root:** `app/src/main/java/com/barryburgle/gameapp/ui/` (~140 Kotlin files)  
**Entry:** `MainActivity` → `GameAppOriginalTheme` → `Navigation`  
**Last updated:** August 2026

---

## Current State Summary

| Area | Status |
|------|--------|
| Theming | 19 custom palettes via `ThemeEnum`; M3 `MaterialTheme`; no dynamic color |
| Typography | Only `bodyMedium` customized in `Typography.kt`; wrappers in `ui/utilities/text/` |
| Shapes | Centralized in `Shapes.kt` (4/8/14/30 dp) but duplicated inline |
| Spacing | **No design tokens** — ~75 files with hardcoded `.dp` values |
| Responsiveness | `LocalConfiguration.screenWidthDp` only; `material3-window-size-class` **declared but unused** |
| Navigation | Custom floating bottom bar only; portrait locked in `MainActivity` |
| Animations | Ad hoc durations (150–4000 ms); shared `BasicAnimatedVisibility` |
| Edge-to-edge | Partial — `WindowCompat.setDecorFitsSystemWindows(false)` + gradient hacks |
| Previews | **Zero** `@Preview` composables |
| Charts/maps | `AndroidView` wrappers (MPAndroidChart, osmdroid) |

---

## 1. Centralized Design Tokens & Layout System

### 1.1 Problem: Magic Numbers Today

Spacing, sizing, and elevation are scattered across the codebase. The only centralized constants are:

**`ui/theme/Shapes.kt`** — corner radii:

```7:12:app/src/main/java/com/barryburgle/gameapp/ui/theme/Shapes.kt
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(30.dp)
)
```

**`ui/utilities/DialogConstant.kt`** — dialog column widths + one font size:

```6:13:app/src/main/java/com/barryburgle/gameapp/ui/utilities/DialogConstant.kt
class DialogConstant {
    companion object {
        val DESCRIPTION_FONT_SIZE = 13.sp
        val TIME_COLUMN_WIDTH = 130.dp
        val LEAD_COLUMN_WIDTH = 130.dp
        val ADD_LEAD_COLUMN_WIDTH = 40.dp
    }
}
```

**`ui/navigation/Navigation.kt`** — screen-level insets passed to all four tabs:

```146:148:app/src/main/java/com/barryburgle/gameapp/ui/navigation/Navigation.kt
        val spaceFromLeft = 16.dp
        val spaceFromTop = 20.dp
        val spaceFromBottom = 60.dp
```

**Recurring hardcoded values found in production code:**

| Token candidate | Current usages | Example files |
|---------------|----------------|---------------|
| `5.dp` | Inner card padding, spacers | `GenericSettingsCard`, `EventCard`, `CountSetting` |
| `7.dp`, `8.dp` | Micro-gaps | `PinPointScatterCard`, histogram sections |
| `10.dp` | Dialog spacers, shadows context | `SetDialog`, `SessionDialog`, entity dialogs |
| `16.dp` | Standard card padding | `GenericSettingsCard`, `StatsCard`, `HeatmapCard` |
| `18.dp` | Chart section gaps | `OutputScreen` |
| `25.dp` | Header icon height | Multiple `*Card.kt` files |
| `30.dp` | Top spacer, bottom nav shape | `StatsScreen`, `Navigation.kt` (duplicates `Shapes.extraLarge`) |
| `34.dp` | Bottom nav horizontal inset | `Navigation.kt` |
| `50.dp` | Icon/button hit area | `MultiChoiceButton`, `DescribedIcon` |
| `60.dp` | Bottom content clearance | `Navigation.kt` → all screens |
| `80.dp` | FAB clearance | `InputScreen.kt` (`spaceFromNavBar`) |
| `100.dp`, `110.dp`, `120.dp` | Fixed control widths | `DeleteCard`, `OutputScreen`, `gameTopBar` |
| `160.dp`, `200.dp`, `250.dp`, `320.dp` | Fixed chart/card dimensions | `StatsScreen`, `LeadDialog`, `HeatmapCalendar` |
| `450.dp` | Map dialog height | `MapDialog.kt` |
| Elevations `5/6/8/10/12/15.dp` | Cards, dialogs, nav bar | 25+ files |

### 1.2 Proposed `AppSpacing` Object

Create `ui/theme/AppSpacing.kt`:

```kotlin
object AppSpacing {
    // Base grid (4dp increments)
    val xxs = 4.dp    // tight inline gaps
    val xs  = 8.dp    // icon-to-label, chip padding
    val sm  = 12.dp   // compact list item padding
    val md  = 16.dp   // standard screen/card padding  ← replaces Navigation spaceFromLeft
    val lg  = 20.dp   // section gaps                   ← replaces spaceFromTop
    val xl  = 24.dp   // card section separation
    val xxl = 32.dp   // major section breaks

    // Semantic layout
    val screenHorizontal = md          // 16.dp — was spaceFromLeft
    val screenTop = lg                 // 20.dp — was spaceFromTop
    val contentAboveBottomNav = 60.dp  // clearance for floating pill nav
    val fabClearance = 80.dp           // InputScreen spaceFromNavBar
    val navBarHorizontalInset = 34.dp  // Navigation.kt pill margins
    val navBarBottomInset = 18.dp

    // Component internals
    val cardInner = md                 // GenericSettingsCard 16.dp padding
    val cardInnerTight = 5.dp          // nested column padding
    val listItemGap = md               // LazyColumn spacedBy(spaceFromLeft)
    val dialogSectionGap = 10.dp
    val dialogFieldGap = 5.dp

    // Fixed component sizes (prefer % or constraints on tablets — see §2)
    val iconSm = 22.dp     // bottom nav icons
    val iconMd = 25.dp     // card header icons
    val iconLg = 50.dp     // MultiChoiceButton, DescribedIcon
    val buttonHeight = 48.dp
    val chartHeightCompact = 250.dp
    val chartHeightDefault = 320.dp
    val mapDialogHeight = 450.dp
}
```

**Migration map (high-impact first):**

1. Replace `Navigation.kt` `spaceFromLeft/Top/Bottom` with `AppSpacing.screenHorizontal`, `screenTop`, `contentAboveBottomNav`.
2. Replace `GenericSettingsCard` `padding(16.dp)` / `padding(5.dp)` with `AppSpacing.cardInner` / `cardInnerTight`.
3. Replace dialog `shadow(elevation = 10.dp)` with `AppElevation.dialog` (see below).
4. Replace `StatsScreen` `heigh = 250.dp; width = 320.dp` with `AppSpacing.chartHeightCompact` and a responsive width helper.
5. Deprecate `DialogConstant` dp values — move into `AppSpacing` or `AppDialogDimens`.

### 1.3 Proposed `AppElevation` Object

Centralize the 5/6/8/10/12/15 dp elevation sprawl:

```kotlin
object AppElevation {
    val card = 5.dp           // StatsCard, OutputCard, InputScreen cards
    val cardRaised = 6.dp     // Timeline bubble
    val navBar = 8.dp         // Navigation Surface tonalElevation
    val navBarShadow = 12.dp  // Navigation Surface shadowElevation
    val dialog = 10.dp        // SessionDialog, DeleteDialog, EventCard, etc.
    val chipSelected = 15.dp  // MultiChoiceButton
}
```

Replace inline `Modifier.shadow(elevation = 10.dp)` in `SessionDialog.kt`, `DateDialog.kt`, `SetDialog.kt`, `ChallengeDialog.kt`, `LeadDialog.kt`, `DeleteDialog.kt`, `MapDialog.kt`, `InfoDialog.kt`, `GenericSortingButton.kt`, `GenericSelectingButton.kt`.

### 1.4 Typography Hierarchy

**Current:** `ui/theme/Typography.kt` overrides only `bodyMedium` at 16.sp. Title/body wrappers default to `onPrimary` — semantically incorrect on `surface` cards:

```7:13:app/src/main/java/com/barryburgle/gameapp/ui/utilities/text/title/LargeTitleText.kt
fun LargeTitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
}
```

**Proposed `Typography.kt` expansion:**

```kotlin
val Typography = Typography(
    displayLarge  = TextStyle(fontSize = 57.sp, lineHeight = 64.sp, fontWeight = FontWeight.Normal),
    displayMedium = TextStyle(fontSize = 45.sp, lineHeight = 52.sp, fontWeight = FontWeight.Normal),
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium= TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.SemiBold),
    titleLarge    = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
    titleMedium   = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium),
    titleSmall    = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    bodyLarge     = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium    = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    bodySmall     = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    labelLarge    = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    labelMedium   = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    labelSmall    = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)
```

**Semantic text composables** — refactor `ui/utilities/text/` to accept optional `color` defaulting to role-based tokens:

| Composable | M3 style | Default color | Usage in app |
|------------|----------|---------------|--------------|
| `LargeTitleText` | `titleLarge` | `onSurface` | `GenericSettingsCard`, screen headers |
| `MediumTitleText` | `titleMedium` | `onSurface` | Section titles |
| `SmallTitleText` | `titleSmall` | `onSurfaceVariant` | Card subtitles |
| `MediumBodyText` | `bodyMedium` | `onSurface` | Descriptions |
| `LittleBodyText` | `bodySmall` | `onSurfaceVariant` | Metadata, captions |
| `ChartLabelText` *(new)* | `labelSmall` | `onSurfaceVariant` | MPAndroidChart legend replacement |
| `DialogDescriptionText` *(new)* | `bodySmall` | `onSurfaceVariant` | Replaces `DialogConstant.DESCRIPTION_FONT_SIZE = 13.sp` |

**Responsive text sizing guidelines:**

| Breakpoint | Rule |
|------------|------|
| Compact (< 600dp width) | Use M3 defaults as defined above |
| Medium (600–840dp) | Scale `display*` and `headline*` by 1.1× via `MaterialTheme` copy or `CompositionLocal` |
| Expanded (> 840dp) | Cap max font size; use `headlineMedium` instead of `displayLarge` for screen titles to avoid oversized text on tablets |
| Minimum touch targets | Labels on buttons: `labelLarge` minimum; never below 12.sp for readable body |

Use `LocalDensity` + window width class (§2) rather than raw `LocalConfiguration.screenWidthDp` for text scaling.

### 1.5 Shape Standardization (`AppShapes`)

Extend existing `Shapes.kt` — do not duplicate radii inline:

```kotlin
// ui/theme/Shapes.kt — keep M3 Shapes, add semantic aliases
object AppShapes {
    val chip = Shapes.small           // 4.dp  — HeatmapCalendar cells, small badges
    val card = Shapes.medium          // 8.dp  — compact cards
    val dialog = Shapes.large         // 14.dp — GenericSettingsCard, StatsCard
    val bottomNav = Shapes.extraLarge // 30.dp — Navigation pill (replace inline RoundedCornerShape(30.dp))
    val fab = CircleShape
    val button = RoundedCornerShape(percent = 50) // GenericShadowButton — use CircleShape or extraLarge consistently
}
```

**Fix today:** `Navigation.kt` line 95 uses `RoundedCornerShape(30.dp)` — should be `MaterialTheme.shapes.extraLarge`. `GenericShadowButton` uses `RoundedCornerShape(30.dp)` for glowing state — align with `AppShapes.bottomNav`.

### 1.6 Semantic Colors (`Colors.kt`)

Existing `ui/theme/Colors.kt` defines greys and alert colors used outside M3 roles:

```19:21:app/src/main/java/com/barryburgle/gameapp/ui/theme/Colors.kt
val AlertLow = Color(0xFF72B043)
val AlertMid = Color(0xFFF8CC1B)
val AlertHigh = Color(0xFFB6042A)
```

Extend with an `AppColors` semantic layer for domain UI:

```kotlin
object AppColors {
    val heatmapEmpty = Grey85
    val heatmapLow = AlertLow.copy(alpha = 0.4f)
    val heatmapMid = AlertMid.copy(alpha = 0.7f)
    val heatmapHigh = AlertHigh
    val liveSessionPulse = /* extract from InputScreen.liveSessionPulsingColor */
    val chartGrid = Grey50.copy(alpha = 0.3f)
}
```

Use these in `HeatmapCalendar.kt`, `OutputPieChart.kt`, and chart wrappers instead of inline hex/alpha values.

### 1.7 Layout Helpers

Create `ui/theme/AppLayout.kt`:

```kotlin
@Composable
fun rememberContentWidth(horizontalPadding: Dp = AppSpacing.screenHorizontal): Dp {
    val config = LocalConfiguration.current
    return (config.screenWidthDp.dp - horizontalPadding * 2)
}

@Composable
fun Modifier.screenContentPadding(): Modifier = this.padding(
    start = AppSpacing.screenHorizontal,
    end = AppSpacing.screenHorizontal,
    bottom = AppSpacing.contentAboveBottomNav
)
```

Replace repeated pattern in `StatsScreen.kt`:

```kotlin
.width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
```

---

## 2. Multi-Device Adaptability & Responsiveness

### 2.1 Current Limitations

1. **Portrait lock** in `MainActivity.onCreate()`:

```176:176:app/src/main/java/com/barryburgle/gameapp/MainActivity.kt
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
```

2. **`material3-window-size-class`** is in `app/build.gradle` but never imported.

3. **Single-column `LazyColumn`** on all four tabs — no list-detail, no grid on tablets.

4. **Fixed chart dimensions** — `StatsScreen` hardcodes `250.dp × 320.dp`; wastes space on foldables/tablets.

5. **Scaffold padding ignored** — `Navigation.kt` receives `padding` from `Scaffold` but does not apply it to `NavHost`; screens manually offset with magic numbers.

### 2.2 Window Size Class Strategy

Introduce a top-level provider in `GameAppOriginalTheme` or `Navigation`:

```kotlin
// ui/theme/WindowSize.kt
@Composable
fun rememberAppWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(activity)
}

enum class AppLayoutType { Compact, Medium, Expanded }

@Composable
fun rememberAppLayoutType(): AppLayoutType = when (rememberAppWindowSizeClass().widthSizeClass) {
    WindowWidthSizeClass.Compact -> AppLayoutType.Compact
    WindowWidthSizeClass.Medium  -> AppLayoutType.Medium
    WindowWidthSizeClass.Expanded-> AppLayoutType.Expanded
    else -> AppLayoutType.Compact
}
```

### 2.3 Adaptive Layout Guidelines by Screen

| Screen | Compact (phone) | Medium (foldable inner / small tablet) | Expanded (tablet / foldable open) |
|--------|-----------------|----------------------------------------|-----------------------------------|
| **Game** (`InputScreen`) | Single-column list + FAB | Wider cards via `rememberContentWidth()`; optional 2-column summary row | List-detail: event list left (40%), detail/dialog panel right (60%) |
| **Dashboard** (`OutputScreen`) | Vertical chart stack | 2-column grid for bar/line charts | 3-column dashboard grid; heatmap full width |
| **Results** (`StatsScreen`) | Single column, `chartHeightCompact` | Side-by-side histogram pairs | Navigation rail + 2-column stat cards; map fullscreen button |
| **Settings** (`ToolsScreen`) | Single column cards | 2-column card grid | Permanent settings list + detail pane |

**Implementation pattern for charts:**

```kotlin
@Composable
fun AdaptiveChartHeight(): Dp {
    return when (rememberAppLayoutType()) {
        AppLayoutType.Compact  -> AppSpacing.chartHeightCompact  // 250.dp
        AppLayoutType.Medium   -> 360.dp
        AppLayoutType.Expanded -> 420.dp
    }
}

@Composable
fun AdaptiveChartWidth(): Dp {
    val layout = rememberAppLayoutType()
    val contentWidth = rememberContentWidth()
    return when (layout) {
        AppLayoutType.Compact  -> minOf(contentWidth, 320.dp)
        AppLayoutType.Medium   -> contentWidth / 2 - AppSpacing.md
        AppLayoutType.Expanded -> contentWidth / 3 - AppSpacing.md
    }
}
```

Apply to `StatsScreen`, `OutputBarCard`, `PinPointScatterCard`, `HeatmapCalendar`.

### 2.4 Adaptive Navigation

**Current:** Custom floating pill `NavigationBar` in `Navigation.kt` — works well on phones, wastes horizontal space on tablets.

| Window width | Navigation pattern |
|--------------|-------------------|
| **Compact** | Keep current floating bottom bar (brand identity) |
| **Medium** | Bottom bar OR compact `NavigationRail` (72dp) on left — A/B test; rail frees vertical chart space |
| **Expanded** | `PermanentNavigationDrawer` (256dp) with icons + labels; content area uses remaining width |

```kotlin
@Composable
fun AppNavigation(/* state + events */) {
    when (rememberAppLayoutType()) {
        AppLayoutType.Compact -> CompactBottomNavScaffold(...)
        AppLayoutType.Medium  -> NavigationRailScaffold(...)
        AppLayoutType.Expanded-> PermanentDrawerScaffold(...)
    }
}
```

Preserve existing routes (`Screen.InputScreen.route = "sessions"`, etc.) — only change chrome.

### 2.5 Foldable & Orientation

1. **Remove unconditional portrait lock** — replace with:

```kotlin
// AndroidManifest.xml — remove forced portrait if present
// MainActivity — only lock on Compact if desired:
if (rememberAppWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact) {
    requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
} else {
    requestedOrientation = SCREEN_ORIENTATION_UNSPECIFIED
}
```

2. **Foldable posture:** Use Jetpack `WindowInfoTracker` (optional phase 2) to detect tabletop/half-open and switch `OutputScreen` to dual-pane (chart + legend side-by-side).

3. **`BoxWithConstraints`** for dialog max width:

```kotlin
BoxWithConstraints {
    val dialogWidth = when {
        maxWidth < 400.dp -> maxWidth - AppSpacing.xl
        maxWidth < 840.dp -> 400.dp
        else -> 480.dp
    }
    SessionDialog(modifier = Modifier.width(dialogWidth), ...)
}
```

Apply to `SessionDialog.kt`, `LeadDialog.kt` (currently fixed `200.dp` fields), `MapDialog.kt` (`450.dp` height → min(450.dp, maxHeight * 0.7f)).

### 2.6 Edge-to-Edge & System Insets

**Current partial implementation:**

- `WindowCompat.setDecorFitsSystemWindows(window, false)` — called **after** `setContent` in `MainActivity` (move **before** `setContent` to avoid one-frame flash).
- `navigationBarsPadding()` — bottom nav only.
- `BlurStatusBar()` — gradient hack using `statusBarHeight * 3/2`:

```16:17:app/src/main/java/com/barryburgle/gameapp/ui/utilities/BlurStatusBar.kt
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() * 3 / 2
```

- `InputScreen.gameTopBar()` — separate status bar fill (inconsistent with other tabs).

**Target architecture:**

```kotlin
// MainActivity.onCreate — BEFORE setContent
enableEdgeToEdge() // androidx.activity.enableEdgeToEdge

// Navigation.kt — apply Scaffold insets properly
Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0), // manual control
    bottomBar = { ... }
) { innerPadding ->
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding) // USE scaffold padding
    ) { ... }
}
```

**Per-screen inset policy:**

| Screen | Top | Bottom | IME |
|--------|-----|--------|-----|
| `InputScreen` | `statusBarsPadding()` on `gameTopBar` | `innerPadding.calculateBottomPadding()` | `imePadding()` on dialogs |
| `OutputScreen`, `StatsScreen`, `ToolsScreen` | Replace `BlurStatusBar` with `Modifier.statusBarsPadding()` + solid `background` | Same as above | Dialogs |
| All dialogs | — | — | `Modifier.imePadding()` on `BasicAlertDialog` content |

**Clipping prevention checklist:**

- Replace `offset(y = spaceFromTop - 20.dp)` anti-pattern in `StatsScreen` with proper `padding(top = ...)`.
- Use `verticalScroll` / `LazyColumn` `contentPadding` instead of negative offsets.
- Add `navigationBarsPadding()` to FAB in `InputScreen` (currently uses fixed `spaceFromNavBar = 80.dp`).
- Test on devices with display cutouts — add `displayCutoutPadding()` to full-bleed charts.

---

## 3. Fluid Animations & Motion Guidelines

### 3.1 Current Animation Inventory

| Pattern | Duration / spec | Files |
|---------|-----------------|-------|
| Nav enter slide | `tween(250ms)` | `Navigation.kt` |
| Nav exit slide + fade | `tween(150ms)` | `Navigation.kt` |
| `BasicAnimatedVisibility` | fade 150ms + spring expand/shrink | `EntitySorter`, `SetDialog`, `DateDialog`, `OutputScreen`, `CreditsCard` |
| FAB menu items | slide vertical, staggered | `InputScreen.kt` (5× `AnimatedVisibility`) |
| FAB rotation | `tween(650ms)` | `InputScreen.kt` |
| Blur overlay | `animateDpAsState`, `tween(350ms)` | `InputScreen.kt` |
| Button press scale | `spring(0.7, 400)` | `GenericShadowButton`, sorting buttons |
| Button glow wave | `tween(2000ms)` infinite | `GenericShadowButton` |
| Switch colors | `animateColorAsState`, `tween(500ms)` | `ThumbColor`, `TrackColor`, `DataExchangeCard` |
| Counter digits | `AnimatedContent` | `InputCounter.kt` |
| Live icon pulse | `rememberInfiniteTransition` | `InputScreen.liveSessionPulsingColor` |
| Challenge shimmer | `tween(4000ms)` infinite | `AchievedChallengeProgressBar.kt` |
| Pie chart highlight | `animateColorAsState` | `OutputPieChart.kt` |

**Shared utility today:**

```14:31:app/src/main/java/com/barryburgle/gameapp/ui/utilities/BasicAnimatedVisibility.kt
fun BasicAnimatedVisibility(visibilityFlag: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visibilityFlag, enter = fadeIn(
            animationSpec = tween(durationMillis = 150)
        ) + expandIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow
            )
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = 150)
        ) + shrinkOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow
            )
        )
    ) { content() }
}
```

### 3.2 Proposed `AppMotion` Spec Object

Create `ui/theme/AppMotion.kt`:

```kotlin
object AppMotion {
    // Durations
    const val DurationFast = 150
    const val DurationMedium = 250
    const val DurationSlow = 350
    const val DurationEmphasis = 500
    const val DurationFabRotation = 650

    // Specs
    val FastTween = tween<Float>(DurationFast)
    val MediumTween = tween<Float>(DurationMedium)
    val EmphasisTween = tween<Float>(DurationEmphasis)

    val GentleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    val ButtonPressSpring = spring<Float>(
        dampingRatio = 0.7f,
        stiffness = 400f
    )

    // Named transitions
    val ScreenEnter = slideInHorizontally(
        initialOffsetX = { it }, animationSpec = tween(DurationMedium)
    )
    val ScreenExit = slideOutHorizontally(
        targetOffsetX = { -it }, animationSpec = tween(DurationFast)
    ) + fadeOut(tween(DurationFast))

    val ExpandCollapse = fadeIn(tween(DurationFast)) + expandIn(GentleSpring)
    val ShrinkCollapse = fadeOut(tween(DurationFast)) + shrinkOut(GentleSpring)
}
```

Migrate `Navigation.kt`, `BasicAnimatedVisibility.kt`, `InputScreen.kt`, `GenericShadowButton.kt`, and switch color animations to use `AppMotion`.

### 3.3 Screen Transition Standards

| Transition | Spec | Apply to |
|------------|------|----------|
| Tab switch (horizontal) | `AppMotion.ScreenEnter/Exit` | `NavHost` composable destinations |
| Dialog open | `fadeIn(250ms) + scaleIn(0.95→1)` | `SessionDialog`, `DeleteDialog`, `InfoDialog` |
| Dialog close | `fadeOut(150ms) + scaleOut` | All dialogs |
| Bottom sheet *(future)* | M3 `ModalBottomSheet` defaults | Lead picker, sort options |
| Shared element | **Not currently used** — defer until list-detail layout on tablets | Session card → detail |

**Do not add shared element transitions until** list-detail split is implemented — current single-column nav has no meaningful shared bounds target.

### 3.4 Performance Best Practices

#### Avoid recomposition traps

| Problem in codebase | Fix |
|---------------------|-----|
| `GenericShadowButton` runs `rememberInfiniteTransition` even when `glowing=false` | Guard: only create transition when `glowing == true` |
| `InputScreen` blur animates full top bar | Apply `blur()` only to overlay Box, not entire `Scaffold` topBar Row |
| Chart `AndroidView` update block reads `MaterialTheme.colorScheme` every recomposition | Wrap color extraction in `remember(colorScheme.primary) { ... }` |
| Multiple `animateColorAsState` in switch rows | Extract `AnimatedSwitchColors` composable; single animation scope |
| FAB expanded menu — 5 separate `AnimatedVisibility` | Consider single `AnimatedVisibility` + `Column` with staggered `animateFloatAsState` on alpha |

#### Use `derivedStateOf`

```kotlin
// InputScreen — avoid recomputing filtered list on every state field change
val visibleEvents by remember {
    derivedStateOf {
        state.allEvents.filter { /* showSessions/showSets/... flags */ }
    }
}
```

Apply similar pattern in `OutputScreen` for chart data transformations and `StatsViewModel`-backed histogram filtering.

#### Infinite animations

Limit concurrent infinite transitions:

| Component | Priority | Rule |
|-----------|----------|------|
| Live session pulse | High (user feedback) | Only animate when `state.isAddingLiveSession` |
| GenericShadowButton wave | Medium | Only when `glowing=true` |
| AchievedChallengeProgressBar shimmer | Low | Only when challenge is active AND visible in viewport |

Use `LazyListState.layoutInfo` to start/stop shimmer for off-screen items.

### 3.5 Micro-Interaction Standards

#### Buttons (`GenericShadowButton`, `IconShadowButton`, sorting buttons)

- **Press:** scale to 0.92, `AppMotion.ButtonPressSpring`, haptic `LongPress` (already in `GenericShadowButton`)
- **Release:** spring back to 1.0
- **Disabled:** alpha 0.38, no scale animation, `clickable(enabled = false)`

#### Lists (`InputScreen` LazyColumn, `EventCard`)

- **Insert/delete:** add `Modifier.animateItem()` (Compose 1.7+) on `EventCard` items
- **Expand/collapse:** use `BasicAnimatedVisibility` / `AnimatedVisibility` with `AppMotion.ExpandCollapse`
- **Swipe-to-delete** *(future)*: `SwipeToDismissBox` with `AlertHigh` background

#### State placeholders

| State | Visual | Composable target |
|-------|--------|-------------------|
| **Loading** | `CircularProgressIndicator` centered, or skeleton shimmer on cards | DAO flow initial load in each screen |
| **Empty** | Illustration + `MediumBodyText("No sessions yet")` + FAB hint | `InputScreen` when `allEvents.isEmpty()` |
| **Error** | `AlertHigh` banner + retry button | CSV import failures in `DataExchangeCard` |
| **Success** | Snackbar or brief `AnimatedVisibility` checkmark | After `SaveAbstractSession` (`justSaved` flag — already exists, add visual) |

Create `ui/utilities/state/EmptyState.kt`, `LoadingState.kt`, `ErrorBanner.kt`.

#### Toggle / switch (`SwitchSetting`, `ThumbColor`, `TrackColor`)

- Unify `ThumbColor.kt` and `TrackColor.kt` into single `AnimatedSwitchColors(checked: Boolean)` using `AppMotion.EmphasisTween` (500ms today — keep).

---

## 4. Component Scalability & Theming

### 4.1 Current Component Architecture

**Strengths:**

- Consistent screen signature: `(state, onEvent, spaceFromLeft, spaceFromTop, spaceFromBottom)`
- Shared utilities in `ui/utilities/` (32 files): buttons, text, settings, selection, dialogs
- Domain cards compose utilities: `GenericSettingsCard` → `ThemeCard`, `BackupCard`, etc.
- MVI-style: stateless screens, events up

**Weaknesses:**

- Utilities mix UI + behavior (`InsertInvite` takes full `StatsState`/`InputState`)
- No `@Preview` on any composable — slows iteration
- `InputState` is ~160 fields with mutable `var` — screens depend on monolithic state
- Theme logic split: `GameAppOriginalTheme` + per-theme active/inactive color maps in `ThemeEnum.useProperActiveColor()`
- Charts/maps bypass Compose theme (`AndroidView` + manual `.toArgb()`)

### 4.2 Reusable Composable Standards

#### Rule 1: Stateless presentation, events up

**Before** (tightly coupled):

```kotlin
InsertInvite(state: InputState, offset: Dp)  // knows about full InputState
```

**After:**

```kotlin
@Composable
fun InsertInviteBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
)
```

#### Rule 2: Modifier as first optional parameter

Follow M3 convention for all new/refactored composables:

```kotlin
@Composable
fun StatsCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
)
```

#### Rule 3: Required previews

Every public composable in `ui/utilities/` must have at least one `@Preview`. Example for `GenericSettingsCard`:

```kotlin
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GenericSettingsCardPreview() {
    GameAppOriginalTheme(theme = ThemeEnum.URBAN.type, themeSysFollow = false) {
        GenericSettingsCard(
            title = "Backup",
            modifier = Modifier.padding(AppSpacing.md)
        ) {
            MediumBodyText("Auto-backup enabled")
        }
    }
}
```

Add preview parameter providers for `ThemeEnum` palettes — iterate 3–4 representative themes (LIGHT, DARK, URBAN, BROODY).

#### Rule 4: Slot-based cards

Standardize domain cards on a shared scaffold:

```kotlin
@Composable
fun AppCard(
    title: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.card),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(AppSpacing.cardInner)) {
            Row(/* title + trailing */) { LargeTitleText(title); trailing?.invoke() }
            content()
        }
    }
}
```

Migrate `StatsCard`, `HeatmapCard`, `OutputBarCard`, `GenericSettingsCard` to `AppCard`.

### 4.3 Theming Integration

#### Current theme resolution

```16:24:app/src/main/java/com/barryburgle/gameapp/ui/theme/GameAppOriginalTheme.kt
    val colors = if (themeSysFollow) {
        if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    } else {
        ThemeEnum.getTheme(theme)
    }
```

**Issue:** When `themeSysFollow=true`, user's selected palette from Settings (`ToolViewModel` → `SetTheme`) is ignored.

**Recommended behavior:**

```kotlin
val colors = when {
    themeSysFollow && isSystemInDarkTheme() -> DarkColorPalette
    themeSysFollow && !isSystemInDarkTheme() -> LightColorPalette
    else -> ThemeEnum.getTheme(theme)
}
// Keep current behavior BUT show clear Settings UX: "Follow system overrides theme selection"
```

#### Dark mode

19 palettes include explicit dark schemes (`DarkColorPalette`, `BroodyColorPalette`, `FreeColorPalette` use `darkColorScheme`; others use `lightColorScheme`).

**Action items:**

1. Audit light-only palettes for dark readability — add `darkColorScheme` variants for top 5 user themes (URBAN, MASTERY, PHOENIX, CROWN, INFINITE).
2. Replace ad-hoc `isSystemInDarkTheme()` in `HeatmapCard.kt`, `LeadDialog.kt` with `MaterialTheme.colorScheme` semantic colors.
3. Fix text wrappers to use `onSurface` / `onSurfaceVariant` instead of `onPrimary`.

#### Dynamic color (Material You) — optional phase

Add as Settings toggle alongside existing theme picker in `ThemeCard.kt`:

```kotlin
val colorScheme = when {
    dynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        if (isSystemInDarkTheme()) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    }
    themeSysFollow -> /* existing */
    else -> ThemeEnum.getTheme(theme)
}
```

When dynamic color is on, disable palette swatches in `ThemeCard` (or show "Dynamic" as exclusive option). `ThemeEnum.useProperActiveColor()` switch maps become no-ops under dynamic color — use `colorScheme.primary` directly.

### 4.4 Chart & Map Theming Bridge

Create `ui/theme/ChartTheme.kt`:

```kotlin
@Composable
fun rememberChartColors(): ChartColors {
    val cs = MaterialTheme.colorScheme
    return remember(cs) {
        ChartColors(
            primary = cs.primary.toArgb(),
            onSurface = cs.onSurface.toArgb(),
            grid = AppColors.chartGrid.toArgb(),
            // ...
        )
    }
}
```

Use in `OutputBarChart.kt`, `OutputLineChart.kt`, `OutputPieChart.kt`, `PinPointScatterChart.kt` to react to theme changes without duplicating `.toArgb()` calls.

**Map migration path:** `MapDialog.kt` TODO references `osdCompose` — plan Compose-native map for theme-aware tiles and preview support.

### 4.5 Component Catalog Structure

Reorganize for scalability (incremental, no big-bang refactor):

```
ui/
├── designsystem/
│   ├── token/          # AppSpacing, AppElevation, AppShapes, AppMotion, AppColors
│   ├── component/      # AppCard, AppButton, AnimatedSwitch, EmptyState
│   └── preview/        # PreviewTheme, PreviewData
├── utilities/          # (legacy — migrate to designsystem/component over time)
├── input/
├── output/
├── stats/
├── tool/
├── navigation/
└── theme/              # GameAppOriginalTheme, Typography, Shapes, palette/*
```

### 4.6 Screen Parameter Migration

Replace manual spacing parameters with composition locals:

```kotlin
// ui/theme/AppSpacingLocals.kt
data class AppLayoutInsets(
    val horizontal: Dp,
    val top: Dp,
    val bottom: Dp
)

val LocalAppLayoutInsets = staticCompositionLocalOf {
    AppLayoutInsets(16.dp, 20.dp, 60.dp) // defaults
}

// Navigation.kt provides:
CompositionLocalProvider(LocalAppLayoutInsets provides AppLayoutInsets(...)) {
    NavHost { ... }
}

// Screens become:
fun InputScreen(state: InputState, onEvent: (GameEvent) -> Unit) {
    val insets = LocalAppLayoutInsets.current
    ...
}
```

Removes `spaceFromLeft/Top/Bottom` from 4 screen signatures and all call sites.

---

## Implementation Roadmap

| Phase | Scope | Files touched |
|-------|-------|---------------|
| **P0 — Tokens** | Add `AppSpacing`, `AppElevation`, `AppMotion`; migrate `Navigation.kt`, `GenericSettingsCard`, dialogs | ~15 files |
| **P1 — Insets** | Fix edge-to-edge ordering; apply Scaffold padding; remove negative offsets | `MainActivity.kt`, `Navigation.kt`, `StatsScreen.kt`, `InputScreen.kt` |
| **P2 — Typography** | Expand `Typography.kt`; fix text wrapper colors; add previews for utilities | `Typography.kt`, `ui/utilities/text/**` |
| **P3 — Responsive** | Wire `WindowSizeClass`; adaptive chart sizes; optional portrait unlock on medium+ | `StatsScreen.kt`, `OutputScreen.kt`, `Navigation.kt` |
| **P4 — Components** | `AppCard`, state placeholders, `ChartTheme` | Cards, charts |
| **P5 — Adaptive nav** | Navigation rail / drawer on expanded | `Navigation.kt` |
| **P6 — Dynamic color** | Optional Material You toggle in `ThemeCard` | `GameAppOriginalTheme.kt`, `ThemeCard.kt` |

---

## Appendix: Key File Reference

| Concern | Path |
|---------|------|
| Root theme | `ui/theme/GameAppOriginalTheme.kt` |
| Palettes (19) | `ui/theme/palette/*.kt` + `model/enums/ThemeEnum.kt` |
| Typography | `ui/theme/Typography.kt` |
| Shapes | `ui/theme/Shapes.kt` |
| Semantic greys/alerts | `ui/theme/Colors.kt` |
| Navigation | `ui/navigation/Navigation.kt`, `Screen.kt` |
| Screen insets (today) | `Navigation.kt` lines 146–148 |
| Text wrappers | `ui/utilities/text/title/*`, `ui/utilities/text/body/*` |
| Animation utility | `ui/utilities/BasicAnimatedVisibility.kt` |
| Primary animated screen | `ui/input/InputScreen.kt` |
| Button animations | `ui/utilities/button/GenericShadowButton.kt` |
| Status bar hack | `ui/utilities/BlurStatusBar.kt` |
| Fixed chart sizes | `ui/stats/StatsScreen.kt` lines 50–51 |
| Settings card pattern | `ui/tool/GenericSettingsCard.kt` |
| Theme picker | `ui/tool/ThemeCard.kt` |
| Window size dependency | `app/build.gradle` → `material3-window-size-class1` |
