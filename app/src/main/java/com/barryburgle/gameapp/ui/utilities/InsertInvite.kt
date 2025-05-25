package com.barryburgle.gameapp.ui.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.ui.state.AllEntityState

@Composable
fun InsertInvite(state: AllEntityState) {
    if (state.allSessions.isEmpty() &&
        state.allDates.isEmpty() &&
        state.allSets.isEmpty() &&
        state.allLeads.isEmpty()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Insert a new:",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                entityDescription(
                    "Session",
                    imageVector = Icons.Default.GroupAdd
                )
                Spacer(modifier = Modifier.height(12.dp))
                entityDescription(
                    "Set",
                    imageVector = Icons.Default.PersonAddAlt1
                )
                Spacer(modifier = Modifier.height(12.dp))
                entityDescription(
                    "Date",
                    imageVector = Icons.Default.Favorite
                )
            }
        }
    }
}

@Composable
private fun entityDescription(
    entityName: String,
    imageVector: ImageVector?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entityName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            if (imageVector != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = imageVector, contentDescription = "session"
                )
            }
        }
    }
}