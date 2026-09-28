package com.barryburgle.gameapp.ui.input.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent
import com.barryburgle.gameapp.model.date.DateModel
import com.barryburgle.gameapp.model.date.DatePhase
import com.barryburgle.gameapp.ui.input.card.DateModelCard
import com.barryburgle.gameapp.ui.input.dialog.text.WavyPlaceholder
import com.barryburgle.gameapp.ui.utilities.animation.AnimatedStaggeredItem
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dialog.FlowDialog
import com.barryburgle.gameapp.ui.utilities.selection.DottedHorizontalPager
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import com.barryburgle.gameapp.ui.utilities.text.title.MediumTitleText

@Composable
fun DateMetronomeDialog(
    allDateModels: List<DateModel> = emptyList(),
    allDatePhases: List<DatePhase> = emptyList(),
    onEvent: (GameEvent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { allDateModels.size })
    val listState = rememberLazyListState()

    val currentSelectedModel by remember(allDateModels, pagerState.currentPage) {
        derivedStateOf {
            allDateModels.getOrNull(pagerState.currentPage)
        }
    }

    val currentPhases by remember(allDatePhases, currentSelectedModel) {
        derivedStateOf {
            val phaseIds = currentSelectedModel?.phases ?: emptyList()
            allDatePhases.filter { it.id in phaseIds }.reversed()
        }
    }

    FlowDialog(
        modifier = Modifier.fillMaxHeight(0.6f),
        onDismissRequest = {
            onEvent(GameEvent.HideDateMetronomeDialog)
            onEvent(GameEvent.SetIsInOverlayToFalse)
        },
        onConfirm = {
            onEvent(GameEvent.HideDateMetronomeDialog)
            onEvent(GameEvent.SetIsInOverlayToFalse)
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    LargeTitleText("Date Metronome", true)
                    WavyPlaceholder("Create, edit and execute your date models")
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconShadowButton(
                        onClick = { onEvent(GameEvent.EditDateModel(null)) },
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add date model"
                    )
                }
            }
        }
    ) { contentPadding ->
        if (allDateModels.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                WavyPlaceholder("Add some date models, they will come handy!")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(75.dp))
                DottedHorizontalPager(
                    items = allDateModels,
                    modifier = Modifier
                        .fillMaxWidth(),
                    pageSpacing = 4.dp,
                    pagerState = pagerState
                ) { dateModel, page ->
                    AnimatedStaggeredItem(index = page - 1) {
                        HorizontallyPagedCard(
                            title = dateModel.title,
                            description = dateModel.description,
                            firstActionButtonIcon = Icons.Default.PlayArrow,
                            firstActionButtonIconGlowing = true,
                            onFirstActionButtonClick = {
                                // TODO: schedule notifications for all the phases
                            },
                            onShareActionButtonClick = {
                                // TODO: copy past model desc + phases properly described
                            },
                            onTouchToEditClick = {
                                onEvent(GameEvent.EditDateModel(dateModel))
                            })
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        WavyPlaceholder(
                            "Edit each one of the date phases in your runnable date models. The Metronome will send you a notification when the next date phase should start!",
                            Modifier.fillMaxWidth()
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconShadowButton(
                            onClick = {
                                currentSelectedModel?.let { model ->
                                    onEvent(
                                        GameEvent.EditDatePhase(
                                            null,
                                            model.id
                                        )
                                    )
                                }
                            },
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add date phase"
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 24.dp)
                    ) {
                        items(
                            count = currentPhases.size,
                            key = { index -> currentPhases[index].id }
                        ) { index ->
                            val datePhase = currentPhases[index]
                            val relativeIndex =
                                (index - listState.firstVisibleItemIndex).coerceAtLeast(0)

                            var totalDragOffsetY by remember { mutableFloatStateOf(0f) }
                            val itemHeightPx =
                                64f * 3f // Approximation for item row threshold calculation

                            AnimatedStaggeredItem(index = relativeIndex) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Drag handle section
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 8.dp, end = 4.dp)
                                            .pointerInput(currentSelectedModel, index) {
                                                detectDragGestures(
                                                    onDragStart = { totalDragOffsetY = 0f },
                                                    onDragEnd = { totalDragOffsetY = 0f },
                                                    onDragCancel = { totalDragOffsetY = 0f },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        totalDragOffsetY += dragAmount.y

                                                        val model = currentSelectedModel
                                                            ?: return@detectDragGestures
                                                        val targetIndex = when {
                                                            totalDragOffsetY > itemHeightPx && index < currentPhases.size - 1 -> index + 1
                                                            totalDragOffsetY < -itemHeightPx && index > 0 -> index - 1
                                                            else -> null
                                                        }

                                                        if (targetIndex != null) {
                                                            val updatedDisplayList =
                                                                currentPhases.toMutableList()
                                                            val movedPhase =
                                                                updatedDisplayList.removeAt(index)
                                                            updatedDisplayList.add(
                                                                targetIndex,
                                                                movedPhase
                                                            )

                                                            // Re-reverse back to synchronize with DateModel's original order
                                                            val updatedPhaseIds =
                                                                updatedDisplayList.reversed()
                                                                    .map { it.id }
                                                            model.phases = updatedPhaseIds

                                                            onEvent(GameEvent.EditDateModel(model))
                                                            totalDragOffsetY = 0f
                                                        }
                                                    }
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DragHandle,
                                            contentDescription = "Reorder date phase",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(0.55f)
                                            .padding(vertical = 4.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        LittleBodyText(text = "After ${datePhase.duration} minutes")
                                        MediumTitleText(datePhase.title)
                                        LittleBodyText(datePhase.description)
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconShadowButton(
                                            onClick = {
                                                currentSelectedModel?.let { model ->
                                                    onEvent(
                                                        GameEvent.EditDatePhase(
                                                            datePhase,
                                                            model.id
                                                        )
                                                    )
                                                }
                                            },
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit phase"
                                        )
                                        IconShadowButton(
                                            onClick = { onEvent(GameEvent.DeleteDatePhase(datePhase.id)) },
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete phase"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}