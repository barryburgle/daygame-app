package com.barryburgle.gameapp.ui.tool

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.setting.CountSetting

@ExperimentalMaterial3Api
@Composable
fun DashboardCard(
    state: ToolsState,
    modifier: Modifier,
    onEvent: (ToolEvent) -> Unit
) {
    GenericSettingsCard("Dashboard", modifier) {
        CountSetting(
            text = "Average last ${state.lastSessionAverageQuantity} values",
            count = state.lastSessionAverageQuantity,
            description = "Show the moving average for the last ${state.lastSessionAverageQuantity} values on the chart (min 1 to max 10)",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetLastSessionAverageQuantity
        )
        CountSetting(
            text = "Show last ${state.lastSessionsShown} sessions on charts",
            count = state.lastSessionsShown,
            description = "Show only the last ${state.lastSessionsShown} sessions on the dashboard charts (min 1 to max 15)",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetLastSessionsShown
        )
        Spacer(modifier = Modifier.width(5.dp))
        CountSetting(
            text = "Show last ${state.lastWeeksShown} weeks on charts",
            count = state.lastWeeksShown,
            description = "Show only the last ${state.lastWeeksShown} weeks on the dashboard charts (min 1 to max 12)",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetLastWeeksShown
        )
        Spacer(modifier = Modifier.width(5.dp))
        CountSetting(
            text = "Show last ${state.lastMonthsShown} months on charts",
            count = state.lastMonthsShown,
            description = "Show only the last ${state.lastMonthsShown} months on the dashboard charts (min 1 to max 12)",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetLastMonthsShown
        )
        Spacer(modifier = Modifier.width(5.dp))
        CountSetting(
            text = "Show ${state.shownNationalities} nationalities",
            count = state.shownNationalities,
            description = "Show only ${state.shownNationalities} countries in nationalities bar charts and country selector on lead insertion (min 1 to max 8)",
            onEvent = onEvent as (GenericEvent) -> Unit,
            saveEvent = ToolEvent::SetShownNationalities
        )
    }
}