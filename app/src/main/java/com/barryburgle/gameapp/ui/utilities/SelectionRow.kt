package com.barryburgle.gameapp.ui.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.FieldEnum
import com.barryburgle.gameapp.model.enums.SortType

@Composable
fun SelectionRow(
    currentSort: FieldEnum,
    sortType: FieldEnum,
    onEvent: (GenericEvent) -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            onEvent(
                AbstractSessionEvent.SortSessions(
                    sortType as SortType
                )
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textBackgroundColor =
            if (currentSort == sortType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        val borderColor =
            if (currentSort == sortType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        val textButtonColor =
            if (currentSort == sortType) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
        Column(
            modifier = Modifier
                .background(
                    color = textBackgroundColor, shape = RoundedCornerShape(5.dp)
                )
                .border(1.dp, borderColor, shape = RoundedCornerShape(5.dp))
                .padding(8.dp)
        ) {
            ClickableText(
                onClick = {
                    onEvent(
                        AbstractSessionEvent.SortSessions(
                            sortType as SortType
                        )
                    )
                },
                modifier = Modifier.background(color = textBackgroundColor),
                style = TextStyle(color = textButtonColor),
                text = AnnotatedString(sortType.getField())
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
    }
}