package com.barryburgle.gameapp.database.interoperability

import android.os.Environment
import android.util.Log
import com.barryburgle.gameapp.model.session.AbstractSession
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date


class CsvService {

    companion object {
        fun exportRows(
            exportFolder: String,
            filename: String,
            exportRowList: List<AbstractSession>,
            exportHeader: String
        ) {
            val exportDir = File(Environment.getExternalStorageDirectory(), "/$exportFolder")
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
                val exportArrayHeader =
                    arrayOf(exportHeader)
                csvWrite.writeNext(exportArrayHeader)
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
    }
}