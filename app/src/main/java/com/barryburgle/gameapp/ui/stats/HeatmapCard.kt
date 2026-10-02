package com.barryburgle.gameapp.ui.stats

import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.luminance
import com.barryburgle.gameapp.event.StatsEvent
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.ui.stats.state.StatsState
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.dropdown.Dropdown
import com.barryburgle.gameapp.ui.utilities.dropdown.SelectableOption
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

@ExperimentalMaterial3Api
@Composable
fun HeatmapCard(
    modifier: Modifier,
    title: String,
    statCardIcon: ImageVector,
    description: String,
    state: StatsState,
    onEvent: (StatsEvent) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val selectedTypes = state.mapPinPointsTypeSelectionList.filterIsInstance<PinPointTypeEnum>()

    var themeColorArgb = MaterialTheme.colorScheme.primary.toArgb()
    val isThemeColorArgbDark = themeColorArgb.luminance < 0.5f
    if (!isThemeColorArgbDark) {
        themeColorArgb = MaterialTheme.colorScheme.onPrimary.toArgb()
    }
    val themeRed = android.graphics.Color.red(themeColorArgb)
    val themeGreen = android.graphics.Color.green(themeColorArgb)
    val themeBlue = android.graphics.Color.blue(themeColorArgb)

    val mapInstance = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
            setHasTransientState(true)

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.parent?.requestDisallowInterceptTouchEvent(true)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        view.parent?.requestDisallowInterceptTouchEvent(false)
                    }
                }
                false
            }
        }
    }

    LaunchedEffect(state.typeFilteredMapPinPoints, themeColorArgb) {
        val sortedPinpoints = state.typeFilteredMapPinPoints.sortedBy { it.longitude }
        val geoPoints = sortedPinpoints.map { GeoPoint(it.latitude, it.longitude) }
        val boundingBox =
            if (geoPoints.isNotEmpty()) BoundingBox.fromGeoPoints(geoPoints) else null

        if (boundingBox != null && sortedPinpoints.size > 1) {
            mapInstance.zoomToBoundingBox(boundingBox, false, 90)
        } else if (geoPoints.isNotEmpty()) {
            mapInstance.controller.setZoom(16.5)
            mapInstance.controller.setCenter(geoPoints.first())
        }

        if (sortedPinpoints.isEmpty()) {
            mapInstance.overlays.clear()
            mapInstance.invalidate()
            return@LaunchedEffect
        }

        val animatables = List(sortedPinpoints.size) { Animatable(0f) }

        val renderJob = launch {
            while (isActive) {
                mapInstance.overlays.clear()
                sortedPinpoints.forEachIndexed { index, pinpoint ->
                    val scale = animatables[index].value
                    if (scale > 0.01f) {
                        val center = GeoPoint(pinpoint.latitude, pinpoint.longitude)

                        val targetAlpha = when (pinpoint.pinPointType.lowercase()) {
                            PinPointTypeEnum.SET.getField().lowercase() -> 80
                            PinPointTypeEnum.CONVERSATION.getField().lowercase() -> 120
                            PinPointTypeEnum.CONTACT.getField().lowercase() -> 200
                            else -> 80
                        }

                        val glowLayers = listOf(
                            Pair(15.0 * scale, targetAlpha),
                            Pair(30.0 * scale, (targetAlpha * 0.5f).toInt()),
                            Pair(60.0 * scale, (targetAlpha * 0.15f).toInt())
                        )

                        glowLayers.forEach { (radius, calculatedAlpha) ->
                            mapInstance.overlays.add(Polygon(mapInstance).apply {
                                points = Polygon.pointsAsCircle(center, radius.coerceAtLeast(0.1))
                                fillColor = android.graphics.Color.argb(
                                    calculatedAlpha.coerceIn(0, 255),
                                    themeRed,
                                    themeGreen,
                                    themeBlue
                                )
                                strokeColor = android.graphics.Color.TRANSPARENT
                                setOnClickListener { _, _, _ -> true }
                            })
                        }
                    }
                }
                mapInstance.invalidate()
                withFrameNanos { }
            }
        }

        val animationJobs = sortedPinpoints.indices.map { i ->
            launch {
                delay(i * 10L)
                animatables[i].animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy, // Overshoots / pops diameter then spring-settles
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        animationJobs.joinAll()
        renderJob.cancel()

        mapInstance.overlays.clear()
        sortedPinpoints.forEach { pinpoint ->
            val center = GeoPoint(pinpoint.latitude, pinpoint.longitude)
            val targetAlpha = when (pinpoint.pinPointType.lowercase()) {
                PinPointTypeEnum.SET.getField().lowercase() -> 80
                PinPointTypeEnum.CONVERSATION.getField().lowercase() -> 120
                PinPointTypeEnum.CONTACT.getField().lowercase() -> 200
                else -> 80
            }

            val glowLayers = listOf(
                Pair(15.0, targetAlpha),
                Pair(30.0, (targetAlpha * 0.5f).toInt()),
                Pair(60.0, (targetAlpha * 0.15f).toInt())
            )

            glowLayers.forEach { (radius, calculatedAlpha) ->
                mapInstance.overlays.add(Polygon(mapInstance).apply {
                    points = Polygon.pointsAsCircle(center, radius)
                    fillColor = android.graphics.Color.argb(
                        calculatedAlpha.coerceIn(0, 255),
                        themeRed,
                        themeGreen,
                        themeBlue
                    )
                    strokeColor = android.graphics.Color.TRANSPARENT
                    setOnClickListener { _, _, _ -> true }
                })
            }
        }
        mapInstance.invalidate()
    }

    DisposableEffect(mapInstance) {
        onDispose {
            mapInstance.onDetach()
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapInstance },
                update = { }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = 600f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = statCardIcon,
                                contentDescription = title,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.height(25.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            LargeTitleText(title)
                        }
                        Box {
                            IconShadowButton(
                                onClick = { expanded = true },
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter Types"
                            )
                            Dropdown(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                items = PinPointTypeEnum.entries,
                                onItemClick = { type ->
                                    val isChecked = selectedTypes.contains(type)
                                    val newList = if (isChecked) {
                                        if (selectedTypes.size > 1) selectedTypes - type else selectedTypes
                                    } else {
                                        selectedTypes + type
                                    }
                                    onEvent(StatsEvent.SelectMapPinPointType(newList))
                                }
                            ) { type ->
                                SelectableOption(
                                    customContent = {
                                        Checkbox(
                                            checked = selectedTypes.contains(type),
                                            onCheckedChange = null
                                        )
                                    },
                                    optionName = type.getField()
                                        .replaceFirstChar { it.uppercase() } + "s"
                                )
                            }
                        }
                    }
                    LittleBodyText(description)
                }
            }
        }
    }
}