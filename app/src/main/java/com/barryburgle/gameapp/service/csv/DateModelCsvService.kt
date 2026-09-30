package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.date.DateModel

class DateModelCsvService : AbstractCsvService<DateModel>() {

    companion object {
        private const val DATE_MODEL_BACKUP_FILENAME: String = "date_model_backup"
        private const val DATE_PHASES_SEPARATOR: String = ";"
    }

    override fun getBackupFileName(): String {
        return DATE_MODEL_BACKUP_FILENAME
    }

    override fun exportSingleRow(dateModel: DateModel): Array<String> {
        val dateModelList = mutableListOf<String>()
        dateModelList.add(dateModel.id.toString())
        dateModelList.add(dateModel.title)

        // Handling the nullable description string
        val desc = dateModel.description ?: ""
        dateModelList.add(if (desc.isEmpty()) "" else cleaEmptyField(desc))

        // Joining the List<Long> phases into a single string for CSV storage
        dateModelList.add(dateModel.phases.joinToString(separator = DATE_PHASES_SEPARATOR))

        return dateModelList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val dateModelFieldList = mutableListOf<String>()
        dateModelFieldList.add("id")
        dateModelFieldList.add("title")
        dateModelFieldList.add("description")
        dateModelFieldList.add("phases")
        return dateModelFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): DateModel {
        val phasesString = fields[3]
        val parsedPhases = if (phasesString.isNotBlank()) {
            phasesString.split(DATE_PHASES_SEPARATOR).map { it.toLong() }
        } else {
            emptyList()
        }

        return DateModel(
            id = fields[0].toLong(),
            title = fields[1],
            description = fields[2].takeIf { it.isNotBlank() },
            phases = parsedPhases
        )
    }
}