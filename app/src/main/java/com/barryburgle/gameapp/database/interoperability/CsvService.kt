package com.barryburgle.gameapp.database.interoperability

import android.os.Environment
import android.util.Log
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date


class CsvService {

    companion object {
        val localPath = Environment.getExternalStorageDirectory()
        val batchSessionService = BatchSessionService()
        fun exportRows(
            exportFolder: String,
            filename: String,
            exportRowList: List<AbstractSession>,
            headerToExport: String,
            exportHeader: Boolean
        ) {
            val exportDir = File(localPath, "/$exportFolder")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            val file = File(
                exportDir,
                filename + SimpleDateFormat("_yyyy_MM_dd_HH_mm'.csv'").format(Date())
            )
            try {
                file.createNewFile()
                val csvWrite = CSVWriter(FileWriter(file))
                if (exportHeader) {
                    val exportArrayHeader =
                        arrayOf(headerToExport)
                    csvWrite.writeNext(exportArrayHeader)
                }
                for (exportRow in exportRowList) {
                    val exportArrayRow =
                        arrayOf(exportRow.toString())
                    csvWrite.writeNext(exportArrayRow)
                }
                csvWrite.close()
            } catch (sqlEx: Exception) {
                Log.e("MainActivity", sqlEx.message, sqlEx)
            }
        }

        fun importRows(
            importFolder: String,
            filename: String,
            exportHeader: Boolean,
            separator: String
        ): List<AbstractSession> {
            val csvReader = CSVReader(
                FileReader(
                    "$localPath/$importFolder/$filename"
                )
            )
            var listOfStrings: List<Array<String>> = emptyList()
            listOfStrings = csvReader.readAll().map {
                it
            }
            val startCount: Int = if (exportHeader) 1 else 0
            var abstractSessionList: MutableList<AbstractSession> = mutableListOf()
            for (index in startCount..listOfStrings.lastIndex) {
                var fields: List<String> = listOfStrings.get(index)[0].split(separator)
                abstractSessionList.add(mapImportRow(fields))
            }
            return abstractSessionList
        }

        fun mapImportRow(fields: List<String>): AbstractSession {
            return batchSessionService.init(
                fields[2].substring(0, 10),
                fields[3].substring(11, 16),
                fields[4].substring(11, 16),
                fields[5],
                fields[6],
                fields[7],
                fields[8]
            )
        }
    }
}