package com.barryburgle.gameapp.ui.output.icon

import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton

@Composable
fun LeadContactButtonIcon(lead: Lead) {
    val isNumber = lead.contact == ContactTypeEnum.NUMBER.getField()
    val iconRes =
        if (isNumber) R.drawable.whatsapp_w else R.drawable.instagram_w
    val desc = if (isNumber) "Whatsapp" else "Instagram"
    var isLinkValid = false
    if ((isNumber && lead.contactLookupKey != null) || (!isNumber && lead.instagramUrl != null && lead.instagramUrl!!.isNotBlank())) {
        isLinkValid = true
    }
    val localContext = LocalContext.current
    if (isLinkValid) {
        val uriHandler = LocalUriHandler.current
        IconShadowButton(
            onClick = {
                try {
                    val uri: String = if (isNumber) {
                        Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lead.contactLookupKey
                        ).toString()
                    } else {
                        lead.instagramUrl!!
                    }
                    uriHandler.openUri(uri)
                } catch (e: Exception) {
                    Toast.makeText(
                        localContext,
                        "Could not open $desc link",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            drawableIcon = iconRes,
            contentDescription = "Contact link"
        )
    } else {
        Box(
            modifier = Modifier
                .scale(1.2f)
                .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .clickable(onClick = {
                    Toast.makeText(
                        localContext,
                        "No $desc contact linked",
                        Toast.LENGTH_SHORT
                    ).show()
                }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "Contact link",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(20.dp)
                    .scale(1.2f)
            )
        }
    }
}