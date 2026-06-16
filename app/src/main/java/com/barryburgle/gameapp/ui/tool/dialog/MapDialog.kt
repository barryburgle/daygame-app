package com.barryburgle.gameapp.ui.tool.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.PinPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapDialog(
    pinPoints: List<PinPoint>,
    leads: List<Lead>,
    onDismiss: () -> Unit
) {
    // TODO: center the map around not the first pinpoint but around the center of gravity of all pinpoints, and adjust the zoom level accordingly to show them all
    val defaultCenter = pinPoints.firstOrNull()?.let {
        GeoPoint(it.latitude, it.longitude)
    } ?: GeoPoint(0.0, 0.0)

    // TODO: use confirm and dismiss buttons from other dialogs
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        title = { Text("Pinpoint Locations") },
        text = {
            // TODO [ignore] :instead of using AndroidView use osdCompose lib for this component
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                factory = { context ->
                    MapView(context).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(14.0)
                        controller.setCenter(defaultCenter)
                        pinPoints.forEach { coordinate ->
                            val markerGeoPoint = GeoPoint(coordinate.latitude, coordinate.longitude)
                            // TODO: when touchin the pinpoint in a specific location a search in the leads should happen to find the lead with column pinpoint_id = to pinpoint.id
                            val marker = Marker(this).apply {
                                position = markerGeoPoint
                                title = "Pinpoint"
                                subDescription = "Time: è+è+è+èé*"
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            overlays.add(marker)
                        }
                    }
                },
                update = { mapView ->
                    mapView.controller.setCenter(defaultCenter)
                }
            )
        }
    )
}