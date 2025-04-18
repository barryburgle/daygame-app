package com.barryburgle.gameapp.ui.utilities.button

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GameEvent

@Composable
fun TweetLinkImportButton(
    onEvent: (GameEvent) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val localContext = LocalContext.current.applicationContext
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), onClick = {
        var tweetUrl: String = clipboardManager.getText()!!.toString()
        if (tweetUrl.startsWith("https://x.com/")) {
            onEvent(GameEvent.SetTweetUrl(tweetUrl))
            Toast.makeText(
                localContext,
                "Copied tweet url",
                Toast.LENGTH_SHORT
            ).show()
        }
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = "Tweet Url Import Button",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(15.dp)
            )
            Text(
                text = "Tweet Url",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}