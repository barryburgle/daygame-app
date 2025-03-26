package com.barryburgle.gameapp.service.csv

import android.os.Environment
import java.io.File

class CSVFindService {
    val localPath = Environment.getExternalStorageDirectory()
    fun findCsvFiles(folder: String): List<String> {
        val dir = File(localPath, "/$folder")
        if (!dir.exists()) {
            return listOf()
        }
        return dir.listFiles()?.filter {
            it.toString().endsWith(".csv")
        }?.map {
            it.name
        } ?: listOf()
    }

    fun getLastBackupDate(backupFolder: String): String {
        val filenameList = processBackupFilenames(findCsvFiles(backupFolder))
        if (filenameList != null && filenameList.isNotEmpty()) {
            val firstFilename = filenameList.get(0)
            if (firstFilename != null && firstFilename.isNotBlank()) {
                val backupDate = firstFilename.take(10).split("_").reversed().joinToString("-")
                val backupTime = firstFilename.takeLast(5).replace("_", ":")
                return "Last backup completed at " + backupTime + " on " + backupDate
            }
        }
        return ""
    }

    private fun processBackupFilenames(backupFilenames: List<String>): List<String> {
        return backupFilenames
            .map { filename ->
                filename.removePrefix("sessions_backup_")
                    .removePrefix("leads_backup_")
                    .removePrefix("dates_backup_")
                    .removeSuffix(".csv")
            }
            .toSet()
            .sortedDescending()
    }
}