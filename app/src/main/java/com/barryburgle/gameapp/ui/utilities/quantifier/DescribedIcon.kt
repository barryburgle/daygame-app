package com.barryburgle.gameapp.ui.utilities.quantifier

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescribedIcon(
    trueFlagDescription: String,
    falseFlagDescription: String,
    descriptionFontSize: TextUnit,
    @DrawableRes icon: Int,
    happened: Boolean?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
    ) {
        var color: Color = MaterialTheme.colorScheme.secondaryContainer
        var flagDescription = trueFlagDescription
        if (!happened!!) {
            color = MaterialTheme.colorScheme.inversePrimary
            flagDescription = falseFlagDescription
        }
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(25.dp)
                )
                .size(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = flagDescription,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(30.dp),
                colorFilter = ColorFilter.tint(color)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = flagDescription,
            fontSize = descriptionFontSize,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}