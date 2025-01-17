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
        val separator = ","
        fun exportRows(
            exportFolder: String,
            filename: String,
            abstractSessionList: List<AbstractSession>,
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
                    val exportArrayHeader = generateHeader()
                    csvWrite.writeNext(exportArrayHeader)
                }
                for (abstractSession in abstractSessionList) {
                    val exportArrayRow =
                        exportSingleRow(abstractSession)
                    csvWrite.writeNext(exportArrayRow)
                }
                csvWrite.close()
            } catch (sqlEx: Exception) {
                Log.e("MainActivity", sqlEx.message, sqlEx)
            }
        }

        fun exportSingleRow(abstractSession: AbstractSession): Array<String> {
            val abstractSessionFieldList = mutableListOf<String>()
            abstractSessionFieldList.add(abstractSession.id.toString())
            abstractSessionFieldList.add(abstractSession.insertTime)
            abstractSessionFieldList.add(abstractSession.date)
            abstractSessionFieldList.add(abstractSession.startHour)
            abstractSessionFieldList.add(abstractSession.endHour)
            abstractSessionFieldList.add(abstractSession.sets.toString())
            abstractSessionFieldList.add(abstractSession.convos.toString())
            abstractSessionFieldList.add(abstractSession.contacts.toString())
            abstractSessionFieldList.add(abstractSession.stickingPoints)
            abstractSessionFieldList.add(abstractSession.sessionTime.toString())
            abstractSessionFieldList.add(abstractSession.approachTime.toString())
            abstractSessionFieldList.add(abstractSession.convoRatio.toString())
            abstractSessionFieldList.add(abstractSession.rejectionRatio.toString())
            abstractSessionFieldList.add(abstractSession.contactRatio.toString())
            abstractSessionFieldList.add(abstractSession.index.toString())
            abstractSessionFieldList.add(abstractSession.dayOfWeek.toString())
            abstractSessionFieldList.add(abstractSession.weekNumber.toString())
            return abstractSessionFieldList.toTypedArray()
        }

        fun generateHeader(): Array<String> {
            val abstractSessionFieldList = mutableListOf<String>()
            abstractSessionFieldList.add("id")
            abstractSessionFieldList.add("insert_time")
            abstractSessionFieldList.add("session_date")
            abstractSessionFieldList.add("start_hour")
            abstractSessionFieldList.add("end_hour")
            abstractSessionFieldList.add("sets")
            abstractSessionFieldList.add("convos")
            abstractSessionFieldList.add("contacts")
            abstractSessionFieldList.add("sticking_points")
            abstractSessionFieldList.add("session_time")
            abstractSessionFieldList.add("approach_time")
            abstractSessionFieldList.add("convo_ratio")
            abstractSessionFieldList.add("rejection_ratio")
            abstractSessionFieldList.add("contact_ratio")
            abstractSessionFieldList.add("index")
            abstractSessionFieldList.add("day_of_week")
            abstractSessionFieldList.add("week_number")
            return abstractSessionFieldList.toTypedArray()
        }

        fun importRows(
            importFolder: String,
            filename: String,
            exportHeader: Boolean
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
                abstractSessionList.add(mapImportRow(listOfStrings.get(index)))
            }
            return abstractSessionList
        }

        fun mapImportRow(fields: Array<String>): AbstractSession {
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