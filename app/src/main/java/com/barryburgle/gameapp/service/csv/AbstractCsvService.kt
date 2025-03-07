package com.barryburgle.gameapp.service.csv

import android.os.Environment
import android.util.Log
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

abstract class AbstractCsvService<T : Any> {

    private var exportObjects: List<T>? = null

    protected abstract fun exportSingleRow(singleObject: T): Array<String>

    protected abstract fun generateHeader(): Array<String>

    protected abstract fun mapImportRow(fields: Array<String>): T

    val localPath = Environment.getExternalStorageDirectory()

    fun setExportObjects(list: List<T>) {
        exportObjects = list
    }

    fun exportRows(
        exportFolder: String,
        filename: String,
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
            for (singleObject in exportObjects!!) {
                val exportArrayRow =
                    exportSingleRow(singleObject)
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
        importHeader: Boolean
    ): List<T> {
        val csvReader = CSVReader(
            FileReader(
                "$localPath/$importFolder/$filename"
            )
        )
        var listOfStrings: List<Array<String>> = emptyList()
        listOfStrings = csvReader.readAll().map {
            it
        }
        val startCount: Int = if (importHeader) 1 else 0
        var exportObjects: MutableList<T> = mutableListOf()
        for (index in startCount..listOfStrings.lastIndex) {
            exportObjects.add(mapImportRow(listOfStrings.get(index)))
        }
        return exportObjects
    }

    protected fun importLong(longAsString: String): Long? {
        try {
            return longAsString.toLong()
        } catch (e: NumberFormatException) {
            return null
        }
    }
}