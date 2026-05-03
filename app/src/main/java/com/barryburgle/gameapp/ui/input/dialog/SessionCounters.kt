package com.barryburgle.gameapp.ui.input.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.R
import com.barryburgle.gameapp.ui.input.InputCounter
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun SessionCounters(
    onSetsChange: (Int) -> Unit,
    onConvosChange: (Int, Boolean) -> Unit,
    onContactsChange: (Int, Boolean) -> Unit,
    setsCount: Int,
    convosCount: Int,
    contactsCount: Int,
) {
    // TODO: integrate this in session dialog: it works but only on the backend, leaving stale unchanged values on the dialog
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CounterColumn(
            count = setsCount,
            label = if (setsCount != 1) "Sets" else "Set",
            iconRes = R.drawable.set_action,
            onIncrement = { onSetsChange(setsCount + 1) },
            onDecrement = { onSetsChange(setsCount - 1) }
        )
        CounterColumn(
            count = convosCount,
            label = if (convosCount != 1) "Conversations" else "Conversation",
            iconRes = R.drawable.conversation_action,
            onIncrement = { onConvosChange(convosCount + 1, true) },
            onDecrement = { onConvosChange(convosCount - 1, false) }
        )
        CounterColumn(
            count = contactsCount,
            label = "Contacts",
            iconRes = R.drawable.contact_action,
            onIncrement = { onContactsChange(contactsCount + 1, true) },
            onDecrement = { onContactsChange(contactsCount - 1, false) }
        )
    }
}

@Composable
private fun CounterColumn(
    count: Int,
    label: String,
    @DrawableRes iconRes: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconShadowButton(
            onClick = onDecrement,
            imageVector = Icons.Default.Remove,
            contentDescription = "Less"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            InputCounter(count = count, style = MaterialTheme.typography.titleSmall)
            Image(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier.height(30.dp),
                contentScale = ContentScale.Fit
            )
        }
        LittleBodyText(label)
        Spacer(modifier = Modifier.height(4.dp))
        IconShadowButton(
            onClick = onIncrement,
            imageVector = Icons.Default.Add,
            contentDescription = "More"
        )
    }
}