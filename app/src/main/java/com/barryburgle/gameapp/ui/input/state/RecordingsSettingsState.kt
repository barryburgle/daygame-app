package com.barryburgle.gameapp.ui.input.state

import com.barryburgle.gameapp.dao.setting.SettingDao

data class RecordingsSettingsState(
    val recordingsFolder: String = SettingDao.DEFAULT_RECORDINGS_FOLDER,
    val recordingsEnabled: Boolean = false
)
