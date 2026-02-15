package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.setting.Setting

class SettingCsvService : AbstractCsvService<Setting>() {

    companion object {
        private const val SETTINGS_BACKUP_FILENAME: String = "settings_backup"
    }

    public override fun getBackupFileName(): String {
        return SETTINGS_BACKUP_FILENAME
    }

    override fun exportSingleRow(setting: Setting): Array<String> {
        val settingFieldList = mutableListOf<String>()
        settingFieldList.add(setting.id)
        settingFieldList.add(setting.value)
        return settingFieldList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val settingFieldList = mutableListOf<String>()
        settingFieldList.add("id")
        settingFieldList.add("value")
        return settingFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Setting {
        return Setting(fields[0], fields[1])
    }

    override fun isEntityValid(setting: Setting): Boolean {
        if (setting.id.isNotBlank() && setting.value.isNotBlank()) {
            return false
        }
        return true
    }
}