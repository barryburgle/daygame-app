package com.barryburgle.gameapp.ui.stats

import android.view.MotionEvent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.LargeTitleText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    allPinPoints: List<PinPoint>
) {
    val context = LocalContext.current

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

    LaunchedEffect(allPinPoints) {
        withContext(Dispatchers.Default) {
            val geoPoints = allPinPoints.map { GeoPoint(it.latitude, it.longitude) }
            val boundingBox =
                if (geoPoints.isNotEmpty()) BoundingBox.fromGeoPoints(geoPoints) else null

            val computedGlowOverlays = allPinPoints.map { pinpoint ->
                val center = GeoPoint(pinpoint.latitude, pinpoint.longitude)
                listOf(
                    Pair(Polygon.pointsAsCircle(center, 40.0), 100),
                    Pair(Polygon.pointsAsCircle(center, 80.0), 55),
                    Pair(Polygon.pointsAsCircle(center, 130.0), 20)
                )
            }

            withContext(Dispatchers.Main) {
                mapInstance.overlays.clear()

                computedGlowOverlays.forEach { glowLayers ->
                    glowLayers.forEach { (pointsList, alphaValue) ->
                        mapInstance.overlays.add(Polygon(mapInstance).apply {
                            points = pointsList
                            fillColor = android.graphics.Color.argb(alphaValue, 46, 204, 113)
                            strokeColor = android.graphics.Color.TRANSPARENT
                        })
                    }
                }

                if (boundingBox != null && allPinPoints.size > 1) {
                    mapInstance.zoomToBoundingBox(boundingBox, false, 90)
                } else if (geoPoints.isNotEmpty()) {
                    mapInstance.controller.setZoom(16.5)
                    mapInstance.controller.setCenter(geoPoints.first())
                }
                mapInstance.invalidate()
            }
        }
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
                    }
                    LittleBodyText(description)
                }
            }
        }
    }
}