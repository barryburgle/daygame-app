package com.barryburgle.gameapp.ui.tool.dialog

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.service.FormatService
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDialog(
    pinPoints: List<PinPoint>,
    leads: List<Lead>,
    zoomOnOpen: Double,
    onDismiss: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val errorColor = MaterialTheme.colorScheme.error.toArgb()
    val boundingBox = if (pinPoints.isNotEmpty()) {
        BoundingBox.fromGeoPoints(pinPoints.map { GeoPoint(it.latitude, it.longitude) })
    } else {
        null
    }
    val mapCenter = boundingBox?.centerWithDateLine ?: GeoPoint(0.0, 0.0)
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                // TODO [ignore] :instead of using AndroidView use osdCompose lib for this component
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setMultiTouchControls(true)
                            zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)

                            if (boundingBox != null && pinPoints.size > 1) {
                                post {
                                    zoomToBoundingBox(boundingBox, false, 80)
                                }
                            } else {
                                controller.setZoom(zoomOnOpen)
                                controller.setCenter(mapCenter)
                            }

                            pinPoints.forEach { pinPoint ->
                                val markerGeoPoint = GeoPoint(pinPoint.latitude, pinPoint.longitude)
                                val associatedLead = leads.find { it.pinPointId == pinPoint.id }
                                val markerTitle = if (associatedLead != null)
                                    "${associatedLead?.name} ${
                                        CountryEnum.getFlagByAlpha3(
                                            associatedLead?.nationality!!
                                        )
                                    } ${associatedLead?.age} "
                                else pinPoint.pinPointType.replaceFirstChar { it.uppercase() }
                                val markerSnippet = FormatService.getDate(
                                    pinPoint.utcTimestamp.substring(
                                        0,
                                        16
                                    ) + 'Z'
                                ) + " " +
                                        FormatService.getTime(
                                            pinPoint.utcTimestamp.substring(
                                                0,
                                                16
                                            ) + 'Z'
                                        )
                                val drawableResId = when (pinPoint.pinPointType) {
                                    PinPointTypeEnum.SET.getField() -> com.barryburgle.gameapp.R.drawable.set_action
                                    PinPointTypeEnum.CONVERSATION.getField() -> com.barryburgle.gameapp.R.drawable.conversation_action
                                    PinPointTypeEnum.CONTACT.getField() -> com.barryburgle.gameapp.R.drawable.contact_action
                                    else -> android.R.drawable.ic_menu_myplaces
                                }
                                val backgroundCircle = GradientDrawable().apply {
                                    shape = GradientDrawable.OVAL
                                    setColor(backgroundColor)
                                    setSize(96, 96)
                                }
                                val actionIcon = ContextCompat.getDrawable(ctx, drawableResId)
                                val layeredIcon =
                                    LayerDrawable(arrayOf(backgroundCircle, actionIcon)).apply {
                                        setLayerInset(1, 16, 16, 16, 16)
                                    }
                                val currentMapView = this
                                val marker = Marker(currentMapView).apply {
                                    position = markerGeoPoint
                                    icon = layeredIcon
                                    title = markerTitle
                                    snippet = markerSnippet
                                    subDescription =
                                        if (associatedLead != null) associatedLead.contact.replaceFirstChar { it.uppercase() } else null
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    setInfoWindowAnchor(0.5f, -0.8f)

                                    infoWindow = object : MarkerInfoWindow(
                                        org.osmdroid.library.R.layout.bonuspack_bubble,
                                        currentMapView
                                    ) {
                                        override fun onOpen(item: Any?) {
                                            super.onOpen(item)
                                            mView?.let { bubbleView ->
                                                val bg = GradientDrawable().apply {
                                                    setColor(backgroundColor)
                                                    cornerRadius = 32f
                                                }
                                                bubbleView.background = bg
                                                bubbleView.clipToOutline = true
                                                bubbleView.setPadding(24, 24, 24, 24)
                                                val titleView =
                                                    bubbleView.findViewById<android.widget.TextView>(
                                                        org.osmdroid.library.R.id.bubble_title
                                                    )
                                                titleView?.setTextColor(textColor)

                                                val descriptionView =
                                                    bubbleView.findViewById<android.widget.TextView>(
                                                        org.osmdroid.library.R.id.bubble_description
                                                    )
                                                descriptionView?.setTextColor(textColor)

                                                val subDescriptionView =
                                                    bubbleView.findViewById<android.widget.TextView>(
                                                        org.osmdroid.library.R.id.bubble_subdescription
                                                    )
                                                subDescriptionView?.setTextColor(textColor)
                                            }
                                        }
                                    }

                                    setOnMarkerClickListener { m, _ ->
                                        m.showInfoWindow()
                                        true
                                    }
                                }
                                overlays.add(marker)
                            }
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    ConfirmButton(onClick = onDismiss)
                }
            }
        }
    )
}