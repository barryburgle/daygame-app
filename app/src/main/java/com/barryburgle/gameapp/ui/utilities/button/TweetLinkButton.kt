package com.barryburgle.gameapp.ui.utilities.button

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TweetLinkButton(tweetUrl: String?) {
    val localContext = LocalContext.current.applicationContext
    val uriHandler = LocalUriHandler.current
    // TODO: bring url on session model and cards body too [v1.7.3]
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(30.dp)
                )
                .size(30.dp)
        ) {

            IconShadowButton(
                onClick = {
                    if (tweetUrl != null && tweetUrl!!.isNotBlank()) {
                        uriHandler.openUri(tweetUrl!!)
                    } else {
                        Toast.makeText(
                            localContext,
                            "No tweet url saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Tweet"
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Tweet",
            fontSize = 10.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}