package com.barryburgle.gameapp.ui.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryburgle.gameapp.ui.utilities.button.IconShadowButton
import com.barryburgle.gameapp.ui.utilities.quantifier.DescribedQuantifier


@Composable
fun CounterColumn(
    count: Int,
    label: String,
    @DrawableRes iconRes: Int? = null,
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
            DescribedQuantifier(
                quantity = count.toString(),
                quantityFontSize = 50.sp,
                description = label,
                descriptionFontSize = 10.sp,
                drawableIcon = iconRes
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        IconShadowButton(
            onClick = onIncrement,
            imageVector = Icons.Default.Add,
            contentDescription = "More"
        )
    }
}