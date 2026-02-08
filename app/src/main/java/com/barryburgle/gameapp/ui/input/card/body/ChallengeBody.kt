package com.barryburgle.gameapp.ui.input.card.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.enums.ChallengeTypeEnum
import com.barryburgle.gameapp.ui.utilities.button.TweetLinkButton
import com.barryburgle.gameapp.ui.utilities.quantifier.AchievedChallengeProgressBar
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText

@Composable
fun ChallengeBody(
    bodyTitle: String,
    achievedChallenge: AchievedChallenge,
    countFontSize: TextUnit,
    descriptionFontSize: TextUnit
) {
    LittleBodyText(bodyTitle)
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
            modifier = Modifier.fillMaxWidth()
        ) {
            AchievedChallengeProgressBar(achievedChallenge)
        }
    }
}