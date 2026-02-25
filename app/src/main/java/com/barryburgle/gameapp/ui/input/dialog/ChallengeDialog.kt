package com.barryburgle.gameapp.ui.input.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.ui.input.InputCountComponent
import com.barryburgle.gameapp.ui.input.dialog.component.DialogTextComponent
import com.barryburgle.gameapp.ui.input.state.InputState
import com.barryburgle.gameapp.ui.tool.dialog.ConfirmButton
import com.barryburgle.gameapp.ui.tool.dialog.DismissButton
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText

@Composable
fun ChallengeDialog(
    state: InputState,
    onEvent: (GameEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier.height(580.dp)
) {
    val localContext = LocalContext.current.applicationContext
    var challengeTypesExpanded by remember { mutableStateOf(false) }
    AlertDialog(modifier = modifier.shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(GameEvent.SetIsInOverlayToFalse)
        onEvent(GameEvent.HideDialog)
    }, title = {
        LargeTitleText(text = description)
    }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogTextComponent(state.challengeName, "Challenge name", 60.dp, "") {
                onEvent(GameEvent.SetChallengeName(it))
            }
            Spacer(modifier = Modifier.height(7.dp))
            DialogTextComponent(
                state.challengeDescription,
                "Challenge description",
                80.dp,
                ""
            ) {
                onEvent(GameEvent.SetChallengeDescription(it))
            }
            Spacer(modifier = Modifier.height(7.dp))
            var updatedChallengeType = ChallengeTypeEnum.getDescription(state.challengeType)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenu(
                    modifier = Modifier
                        .width(175.dp)
                        .height(180.dp),
                    expanded = challengeTypesExpanded,
                    onDismissRequest = { challengeTypesExpanded = false }
                ) {
                    ChallengeTypeEnum.values().forEach { challengeType ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = challengeType.getIcon(),
                                        contentDescription = state.challengeType,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .height(15.dp)
                                    )
                                    LittleBodyText(challengeType.getDescription())
                                }
                            },
                            onClick = {
                                onEvent(GameEvent.SetChallengeType(challengeType.getType()))
                                challengeTypesExpanded = false
                            }
                        )
                    }
                }
                IconShadowButton(
                    onClick = {
                        challengeTypesExpanded = true
                    },
                    imageVector = ChallengeTypeEnum.getIcon(state.challengeType),
                    contentDescription = "Challenge type",
                    title = updatedChallengeType,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(35.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                val localContext = LocalContext.current.applicationContext
                IconShadowButton(
                    onClick = {
                        // TODO: put this logic in a centralized service:
                        var tweetUrl: String = clipboardManager.getText()!!.toString()
                        if (tweetUrl.startsWith("https://x.com/")) {
                            onEvent(GameEvent.SetChallengeTweetUrl(tweetUrl))
                            Toast.makeText(
                                localContext,
                                "Copied tweet url",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    imageVector = Icons.Default.ContentPaste,
                    contentDescription = "Tweet Url",
                    title = "Tweet Url",
                    color = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InputCountComponent(
                    inputTitle = if (updatedChallengeType.equals("Type")) "Goal" else updatedChallengeType,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingChallenge) state.defaultChallengeGoal else state.challengeGoal.toInt(),
                    increment = state.incrementChallengeGoal,
                    saveEvent = GameEvent::SetChallengeGoal,
                )
                InputCountComponent(
                    inputTitle = "Days",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    onEvent = onEvent as (GenericEvent) -> Unit,
                    countStart = if (state.isAddingChallenge) 1 else state.challengeDuration.toInt() + 1,
                    saveEvent = GameEvent::SetChallengeDuration,
                )
            }
        }
    }, confirmButton = {
        ConfirmButton {
            if (state.challengeType.isBlank()) {
                Toast.makeText(localContext, "Please choose a type", Toast.LENGTH_SHORT)
                    .show()
            } else {
                onEvent(GameEvent.SaveChallenge)
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideDialog)
                onEvent(GameEvent.SwitchJustSaved)
                Toast.makeText(localContext, "Challenge saved", Toast.LENGTH_SHORT).show()
            }
        }
    },
        dismissButton = {
            DismissButton {
                onEvent(GameEvent.SetIsInOverlayToFalse)
                onEvent(GameEvent.HideDialog)
            }
        }
    )
}