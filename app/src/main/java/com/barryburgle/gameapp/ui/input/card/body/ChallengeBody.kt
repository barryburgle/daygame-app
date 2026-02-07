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
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun ChallengeBody(
    achievedChallenge: AchievedChallenge,
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
            quantity = "${achievedChallenge.challenge.goal}",
            quantityFontSize = countFontSize,
            description = achievedChallenge.challenge.type.replaceFirstChar { it.lowercase() },
            descriptionFontSize = descriptionFontSize
        )
        var achievedToPrint = achievedChallenge.achieved.toString()
        if (ChallengeTypeEnum.isTypeAchievedInteger(achievedChallenge.challenge.type)) {
            achievedToPrint = achievedChallenge.achieved.toInt().toString()
        }
        val challengeTypeValue = ChallengeTypeEnum.getValue(achievedChallenge.challenge.type)
        var achievedDescription =
            challengeTypeValue.getType() + "s " + challengeTypeValue.getSuccessVerb()
        DescribedQuantifier(
            quantity = achievedToPrint,
            quantityFontSize = countFontSize,
            description = achievedDescription,
            descriptionFontSize = descriptionFontSize
        )
        TweetLinkButton(achievedChallenge.challenge.tweetUrl)
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
            val completionRatio =
                (achievedChallenge.achieved.toFloat() / achievedChallenge.challenge.goal * 100).toInt()
            var completionDesc = "Time to get on with some work"
            if (completionRatio > 0) {
                if (completionRatio < 100) {
                    completionDesc = "You are ${completionRatio}% there!"
                } else {
                    completionDesc =
                        "Congratulations! Results exceed expectations by ${completionRatio}%"
                }
            }
            LittleBodyText(completionDesc)
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
                        .fillMaxWidth(completionRatio.toFloat() / 100)
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