package com.barryburgle.gameapp.ui.utilities

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.GenericEvent
import com.barryburgle.gameapp.model.enums.FieldEnum

@Composable
fun SelectionRow(
    currentSort: FieldEnum,
    sortType: FieldEnum,
    onEvent: (GenericEvent) -> Unit,
    genericEvent: GenericEvent
) {
    // TODO: apply shades on every button on UI (but with less elevation wrt to now) [v1.7.3]
    // TODO: create a tripe-state selection process where either don't sort, sort ascending or sort descending by field [v1.7.3]
    // TODO: put on the left of every button (animating similarly to MultiChoiceButton) text either [no-icon = no selection, up-arrow = sort ascending, down-arrow = sort descending]
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.shadow(
            elevation = 15.dp,
            spotColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
        )
    ) {
        val textBackgroundColor =
            if (currentSort == sortType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
        val textButtonColor =
            if (currentSort == sortType) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
        Column(
            modifier = Modifier
                .background(
                    color = textBackgroundColor, shape = RoundedCornerShape(5.dp)
                )
                .padding(8.dp)
        ) {
            ClickableText(
                onClick = { onEvent(genericEvent) },
                modifier = Modifier.background(color = textBackgroundColor),
                style = TextStyle(color = textButtonColor),
                text = AnnotatedString(sortType.getField())
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
    }
}