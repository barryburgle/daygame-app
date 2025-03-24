package com.barryburgle.gameapp.ui.tool

import android.content.pm.PackageInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.ui.tool.state.ToolsState

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
    val csvFindService = CSVFindService()
    Scaffold { padding ->
        val exportCardModifier = Modifier
            .height(610.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        val importCardModifier = Modifier
            .height(480.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        val settingsCardModifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp - spaceFromLeft * 2)
            .shadow(
                elevation = 5.dp, shape = MaterialTheme.shapes.large
            )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    y = spaceFromTop + spaceFromLeft
                ),
            verticalArrangement = Arrangement.spacedBy(spaceFromLeft)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Export",
                        cardSubtitle = "Holding ${state.allSessions.size} sessions and ${state.allLeads.size} leads",
                        state = state,
                        onEvent = onEvent,
                        modifier = exportCardModifier,
                        sessionCsvService = sessionCsvService,
                        leadCsvService = leadCsvService,
                        dateCsvService = dateCsvService
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Import",
                        cardSubtitle = "Found ${csvFindService.findCsvFiles(state.importFolder).size} csv files in your ${state.importFolder} folder",
                        state = state,
                        onEvent = onEvent,
                        modifier = importCardModifier,
                        sessionCsvService = sessionCsvService,
                        leadCsvService = leadCsvService,
                        dateCsvService = dateCsvService
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    SettingsCard(
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier,
                        currentVersion = versionName,
                        sessionCsvService = sessionCsvService,
                        leadCsvService = leadCsvService,
                        dateCsvService = dateCsvService,
                        context = context
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_round),
                        contentDescription = "Daygame App Icon",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )
                    Text(
                        text =
                        buildAnnotatedString {
                            append("Daygame App ")
                            withLink(
                                LinkAnnotation.Url(
                                    url = "https://github.com/barryburgle/daygame-app",
                                    styles = TextLinkStyles(
                                        style = SpanStyle(
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )
                                )
                            ) {
                                append("v$versionName")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = if (!isSystemInDarkTheme()) painterResource(R.drawable.bb_v3w) else painterResource(
                            R.drawable.bb_v3b
                        ),
                        contentDescription = "Barry Burgle",
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .aspectRatio(1f),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )
                    Text(
                        text =
                        buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Url(
                                    url = "https://barryburgle.wordpress.com/",
                                    styles = TextLinkStyles(
                                        style = SpanStyle(
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )
                                )
                            ) {
                                append("Barry Burgle")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(spaceFromTop + spaceFromBottom + spaceFromLeft * 2)
                )
            }
        }
    }
}
