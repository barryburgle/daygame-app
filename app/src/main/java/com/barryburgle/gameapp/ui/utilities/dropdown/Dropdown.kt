package com.barryburgle.gameapp.ui.utilities.dropdown

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.utilities.text.body.MediumBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun <T> Dropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable RowScope.(item: T) -> Unit
) {
    DropdownMenu(
        modifier = modifier.wrapContentSize(),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            content = { itemContent(item) }
                        )
                        Spacer(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onItemClick(item)
                                        onDismissRequest()
                                    }
                                )
                        )
                    }
                },
                onClick = {
                    onItemClick(item)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun SelectableOption(
    optionName: String,
    modifier: Modifier = Modifier,
    customContent: @Composable (() -> Unit)? = null,
    @DrawableRes icon: Int? = null,
    iconDescription: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (customContent != null) {
            customContent()
        } else if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .height(20.dp)
                    .scale(1.2f)
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
        if (optionName.length < 15) {
            MediumBodyText(
                optionName
            )
        } else {
            SmallTitleText(optionName)
        }
    }
}