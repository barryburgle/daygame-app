package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import kotlin.math.truncate

@Composable
fun ChallengeBody(
    challenge: Challenge,
    achieved: Int,
    countFontSize: TextUnit,
    descriptionFontSize: TextUnit
) {
    LittleBodyText("Challenge overview:")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DescribedQuantifier(
            quantity = "${challenge.goal}",
            quantityFontSize = countFontSize,
            description = challenge.type.replaceFirstChar { it.lowercase() },
            descriptionFontSize = descriptionFontSize
        )
        DescribedQuantifier(
            quantity = achieved.toString(),
            quantityFontSize = countFontSize,
            description = "Done",
            descriptionFontSize = descriptionFontSize
        )
        TweetLinkButton(challenge.tweetUrl)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val completionRatio = achieved.toFloat() / challenge.goal
            LittleBodyText("You are ${truncate(completionRatio * 10000) / 100}% there!")
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(5.dp)
                    ),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(completionRatio)
                        .height(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface, // TODO: animate with left-to-right glow in future
                            shape = RoundedCornerShape(5.dp)
                        )
                ) {
                }
            }
        }
    }
}