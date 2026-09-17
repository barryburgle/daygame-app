package com.barryburgle.gameapp.ui.utilities.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barryburgle.gameapp.model.enums.AudioRecordingQualityEnum
import com.barryburgle.gameapp.ui.tool.utils.DiscreteLevelSlider
import com.barryburgle.gameapp.ui.utilities.text.body.LittleBodyText
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText

@Composable
fun SliderSetting(
    text: String,
    description: String? = null,
    sliderCurrentValue: Int,
    valueRange: IntRange,
    onCheckedChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SmallTitleText(text)
            if (description != null) {
                LittleBodyText(description)
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DiscreteLevelSlider(
                sliderCurrentValue,
                valueRange,
                AudioRecordingQualityEnum.getLabels(),
                onCheckedChange
            )
        }
    }
}