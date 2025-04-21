package com.barryburgle.gameapp.service.csv

import android.os.Environment
import android.util.Log
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

abstract class AbstractCsvService<T : Any> {

    private var exportObjects: List<T>? = null

    protected abstract fun exportSingleRow(singleObject: T): Array<String>

    protected abstract fun generateHeader(): Array<String>

    protected abstract fun mapImportRow(fields: Array<String>): T
    protected abstract fun isEntityValid(entity: T): Boolean
    protected abstract fun getBackupFileName(): String

    val localPath = Environment.getExternalStorageDirectory()

    fun setExportObjects(list: List<T>) {
        exportObjects = list
    }

    fun exportRows(
        exportFolder: String, filename: String, exportHeader: Boolean
    ) {
        val exportDir = File(localPath, "/$exportFolder")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file = File(
            exportDir, filename + SimpleDateFormat("_yyyy_MM_dd_HH_mm'.csv'").format(Date())
        )
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            if (exportHeader) {
                val exportArrayHeader = generateHeader()
                csvWrite.writeNext(exportArrayHeader)
            }
            for (singleObject in exportObjects!!) {
                val exportArrayRow = exportSingleRow(singleObject)
                csvWrite.writeNext(exportArrayRow)
            }
            csvWrite.close()
        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }

    fun listFileNamesLike(folder: String, prefix: String, outputPath: Boolean): List<String> {
        return try {
            File("$localPath/$folder")
                .listFiles { _, name -> name.startsWith(prefix + "_") }
                ?.filter { it.isFile }
                ?.sortedByDescending { it.name }
                ?.map {
                    if (outputPath) it.absolutePath else it.name
                }
                ?: emptyList()
        } catch (e: Exception) {
            Log.e("MainActivity", e.message, e)
            emptyList()
        }
    }

    fun cleanBackupFolder(folder: String, leaveLast: Int) {
        val filenames = listFileNamesLike(
            folder,
            getBackupFileName(),
            true
        ).drop(leaveLast)
        filenames.forEach { filename ->
            try {
                File(filename).delete()
            } catch (e: Exception) {
                Log.e("MainActivity", e.message, e)
            }
        }
    }

    fun validateExport(folder: String): Boolean {
        val filenames = listFileNamesLike(folder, getBackupFileName(), false)
        if (filenames != null && filenames.isNotEmpty()) {
            val validationList = importRows(folder, filenames.get(0), true, 1)
            if (validationList == null || validationList.size == 0) {
                return true
            }
            if (isEntityValid(validationList.get(0))) {
                return true
            }
        }
        return false
    }

    fun importRows(
        importFolder: String, filename: String, importHeader: Boolean, rowLimit: Int? = null
    ): List<T> {
        val fullPath = "$localPath/$importFolder/$filename"
        if (!File(fullPath).exists()) {
            throw FileNotFoundException("File '$importFolder/$filename' not found")
        }
        val csvReader = CSVReader(FileReader(fullPath))
        var listOfStrings: List<Array<String>> = emptyList()
        listOfStrings = csvReader.readAll()
        val startCount: Int = if (importHeader) 1 else 0
        var exportObjects: MutableList<T> = mutableListOf()
        var lastIndex = listOfStrings.lastIndex
        if (rowLimit != null) {
            lastIndex = rowLimit
        }
        if (lastIndex > startCount - 1) {
            for (index in startCount..lastIndex) {
                val row = listOfStrings.get(index)
                if (row != null && row.isNotEmpty()) {
                    exportObjects.add(mapImportRow(row))
                }
            }
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

    protected fun cleaEmptyField(field: String?): String {
        return field ?: ""
    }
}