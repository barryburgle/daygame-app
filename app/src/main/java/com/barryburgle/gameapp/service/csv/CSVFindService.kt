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
}