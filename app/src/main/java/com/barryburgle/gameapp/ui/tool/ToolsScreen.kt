package com.barryburgle.gameapp.ui.tool

import android.content.pm.PackageInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.event.ToolEvent
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
    val uriHandler = LocalUriHandler.current
    Scaffold { padding ->
        val dataExchangeCardModifier = Modifier
            .height(370.dp)
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
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    DataExchangeCard(
                        cardTitle = "Import",
                        state = state,
                        onEvent = onEvent,
                        modifier = dataExchangeCardModifier
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(spaceFromLeft))
                    SettingsCard(
                        state = state,
                        onEvent = onEvent,
                        modifier = settingsCardModifier
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://github.com/barryburgle/daygame-app") },
                            text = "Daygame App v$versionName",
                            style = MaterialTheme.typography.titleSmall,
                            textDecoration = TextDecoration.Underline
                        )
                        Text(
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://github.com/barryburgle/daygame-app/releases") },
                            text = "Check latest version",
                            style = MaterialTheme.typography.titleSmall,
                            textDecoration = TextDecoration.Underline
                        )
                    }
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.bb),
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
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://barryburgle.wordpress.com/") },
                            text = "Barry Burgle",
                            style = MaterialTheme.typography.titleSmall,
                            textDecoration = TextDecoration.Underline
                        )
                    }
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
