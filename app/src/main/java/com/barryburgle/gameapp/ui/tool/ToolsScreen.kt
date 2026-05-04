package com.barryburgle.gameapp.ui.tool

import android.content.pm.PackageInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.csv.ChallengeCsvService
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.service.csv.SetCsvService
import com.barryburgle.gameapp.service.csv.SettingCsvService
import com.barryburgle.gameapp.ui.tool.dialog.DeleteDialog
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import com.barryburgle.gameapp.ui.utilities.BlurStatusBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: ToolsState,
    onEvent: (ToolEvent) -> Unit,
    spaceFromLeft: Dp,
    spaceFromTop: Dp,
    spaceFromBottom: Dp
) {
    val context = LocalContext.current
    val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    val sessionCsvService = SessionCsvService()
    val leadCsvService = LeadCsvService()
    val dateCsvService = DateCsvService()
    val setCsvService = SetCsvService()
    val challengeCsvService = ChallengeCsvService()
    val settingCsvService = SettingCsvService()
    val csvFindService = CSVFindService()
    if (state.isCleaning) {
        DeleteDialog(state = state, onEvent = onEvent, "Clear data", csvFindService)
    }
    Scaffold(
        topBar = {
            BlurStatusBar()
        },
    ) { padding ->
        val dataExchangeCardModifier = Modifier
            .height(780.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        val settingsCardModifier =
            Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
                .shadow(
                    elevation = 5.dp, shape = MaterialTheme.shapes.large
                )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop - 20.dp
                ), verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(30.dp)
                )
            }
            val sessionsCountDesc =
                if (state.allSessions.isEmpty()) "" else "${state.allSessions.size} sessions, "
            val leadsCountDesc =
                if (state.allLeads.isEmpty()) "" else "${state.allLeads.size} leads, "
            val setsCountDesc = if (state.allSets.isEmpty()) "" else "${state.allSets.size} sets, "
            val datesCountDesc =
                if (state.allDates.isEmpty()) "" else "${state.allDates.size} dates"
            val challengesCountDesc =
                if (state.allChallenges.isEmpty()) "." else ", ${state.allChallenges.size} challenges."
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Export",
                        cardSubtitle = "Holding ${sessionsCountDesc}${leadsCountDesc}${setsCountDesc}${datesCountDesc}${challengesCountDesc}",
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier,
                        sessionCsvService = sessionCsvService,
                        leadCsvService = leadCsvService,
                        dateCsvService = dateCsvService,
                        setCsvService = setCsvService,
                        challengeCsvService = challengeCsvService,
                        settingCsvService = settingCsvService,
                        csvFindService = csvFindService
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Import",
                        cardSubtitle = "Found ${csvFindService.findCsvFiles(state.importFolder).size} csv files in your import folder",
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier,
                        sessionCsvService = sessionCsvService,
                        leadCsvService = leadCsvService,
                        dateCsvService = dateCsvService,
                        setCsvService = setCsvService,
                        challengeCsvService = challengeCsvService,
                        settingCsvService = settingCsvService,
                        csvFindService = csvFindService
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    BackupCard(
                        cardTitle = "Backup",
                        cardSubtitle = csvFindService.getLastBackupDate(state.exportFolder + "/" + state.backupFolder),
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier,
                        csvFindService = csvFindService
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DeleteCard(
                        cardTitle = "Wipe out",
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    GameCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    ChallengeCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DashboardCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataEntryCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    LiveSessionCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    ThemeCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    ShareCard(
                        state = state, onEvent = onEvent, modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    CreditsCard(
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier,
                        currentVersion = versionName,
                        context = context
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)
                )
            }
        }
    }
}
