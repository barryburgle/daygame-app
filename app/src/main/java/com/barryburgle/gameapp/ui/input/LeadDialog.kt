package com.barryburgle.gameapp.ui.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.event.AbstractSessionEvent
import com.barryburgle.gameapp.model.enums.ContactTypeEnum
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.ui.input.state.InputState

@Composable
fun AddLeadDialog(
    state: InputState,
    onEvent: (AbstractSessionEvent) -> Unit,
    description: String,
    modifier: Modifier = Modifier
        .height(460.dp)
) {
    val lead = Lead()
    val localContext = LocalContext.current.applicationContext
    var expanded by remember { mutableStateOf(false) }
    AlertDialog(modifier = modifier
        .shadow(elevation = 10.dp), onDismissRequest = {
        onEvent(AbstractSessionEvent.HideLeadDialog)
    }, title = {
        Text(text = description)
    }, text = {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(AbstractSessionEvent.SetLeadName(it)) },
                placeholder = { Text(text = "Name") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { expanded = true },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.nationality.isBlank()) "Press to choose a country" else CountryEnum.getCountryNameByAlpha3(
                        state.nationality
                    ),
                    modifier = Modifier
                        .width(150.dp)
                )
                Text(text = CountryEnum.getFlagByAlpha3(state.nationality))
            }
            DropdownMenu(
                modifier = Modifier
                    .width(200.dp)
                    .height(450.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CountryEnum.values().forEach { country ->
                    DropdownMenuItem(
                        text = { Text(text = country.flag + "  " + country.countryName) },
                        onClick = {
                            onEvent(AbstractSessionEvent.SetLeadNationality(country.alpha3))
                            expanded = false
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(60.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        Text(
                            "Number"
                        )
                        Checkbox(
                            checked = state.contact == ContactTypeEnum.NUMBER.getField(),
                            onCheckedChange = {
                                onEvent(AbstractSessionEvent.SetLeadContact(ContactTypeEnum.NUMBER.getField()))
                            }
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(50.dp)
                    ) {
                        Text(
                            "Social Media"
                        )
                        Checkbox(
                            checked = state.contact == ContactTypeEnum.SOCIAL.getField(),
                            onCheckedChange = {
                                onEvent(AbstractSessionEvent.SetLeadContact(ContactTypeEnum.SOCIAL.getField()))
                            }
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(60.dp)
                ) {
                    InputCountComponent(
                        inputTitle = "Age",
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleSmall,
                        onEvent = onEvent,
                        countStart = state.age.toInt(),
                        saveEvent = AbstractSessionEvent::SetLeadAge
                    )
                }
            }
        }
    }, confirmButton = {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.width(250.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onEvent(AbstractSessionEvent.HideLeadDialog)
                    }
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        lead.name = state.name
                        lead.contact = state.contact
                        lead.nationality = state.nationality
                        lead.age = state.age
                        onEvent(AbstractSessionEvent.SetLead(lead))
                        onEvent(AbstractSessionEvent.HideLeadDialog)
                        Toast.makeText(localContext, "Lead on hold", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    })
}