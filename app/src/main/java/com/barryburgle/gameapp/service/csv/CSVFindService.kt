package com.barryburgle.gameapp.service.csv

import android.os.Environment
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CSVFindService {

    companion object {
        const val ARCHIVE_FOLDER: String = "archive"
    }

    val localPath = Environment.getExternalStorageDirectory()
    fun archiveBackups(exportFolder: String, backupFolder: String) {
        val backupRelativePath = "/$exportFolder/$backupFolder"
        var backupFiles: List<String> = findCsvFiles(backupRelativePath)
        val backupDir = File(localPath, backupRelativePath)
        val archiveDir = File(localPath, "/$backupRelativePath/$ARCHIVE_FOLDER")
        if (!archiveDir.exists()) {
            archiveDir.mkdirs()
        }
        for (file in backupFiles) {
            val sourceFile = File(backupDir, file)
            val destinationFile = File(archiveDir, file)
            Files.move(
                sourceFile.toPath(),
                destinationFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        }
    }

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

    fun getLastFilenameInFolder(folder: String, prefix: String): String {
        var foundFiles: List<String> = findCsvFiles(folder)
        val foundFilename: String = foundFiles.filter { foundFile -> foundFile.startsWith(prefix) }
            .toSet()
            .sortedDescending()[0]
        return foundFilename
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
                    .removePrefix("sets_backup_")
                    .removeSuffix(".csv")
            }
            .toSet()
            .sortedDescending()
    }
}