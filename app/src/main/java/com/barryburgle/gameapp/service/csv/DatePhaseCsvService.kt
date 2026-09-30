package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.date.DatePhase

class DatePhaseCsvService : AbstractCsvService<DatePhase>() {

    companion object {
        private const val DATE_PHASE_BACKUP_FILENAME: String = "date_phase_backup"
    }

    override fun getBackupFileName(): String {
        return DATE_PHASE_BACKUP_FILENAME
    }

    override fun exportSingleRow(datePhase: DatePhase): Array<String> {
        val datePhaseList = mutableListOf<String>()
        datePhaseList.add(datePhase.id.toString())
        datePhaseList.add(datePhase.title)
        datePhaseList.add(datePhase.description)
        datePhaseList.add(datePhase.duration.toString())
        return datePhaseList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val datePhaseFieldList = mutableListOf<String>()
        datePhaseFieldList.add("id")
        datePhaseFieldList.add("title")
        datePhaseFieldList.add("description")
        datePhaseFieldList.add("duration")
        return datePhaseFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): DatePhase {
        return DatePhase(
            id = fields[0].toLong(),
            title = fields[1],
            description = fields[2],
            duration = fields[3].toLong()
        )
    }
}