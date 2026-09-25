package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.ping.Ping

class PingCsvService : AbstractCsvService<Ping>() {

    companion object {
        private const val PINGS_BACKUP_FILENAME: String = "pings_backup"
        private const val LIST_DELIMITER = ";"
    }

    public override fun getBackupFileName(): String {
        return PINGS_BACKUP_FILENAME
    }

    /*Pings' local-image links are not exported nor imported because a backup (or any) import would anyway
    * ask for user's permission to access the specific image linked. Unfortunately, there is no way -
    * and it is unsafe - to ask for a generic all-image access to the user on ping import and then let the
    * app show only the images the pings refer to. Even general media access asked on a post-import PingCard
    * does not allow media access and image display. The only found alternative is copying the ping media to
    * the backup folder and store those in the database. Given that those would bloat the app storage,
    * potentially containing sensitive user data, and since pings usually are a few (how many pings do you really
    * want to manage?) we can avoid storing images and after every import (rare event) ask for user re-linking his
    * media back.*/
    override fun exportSingleRow(ping: Ping): Array<String> {
        val pingList = mutableListOf<String>()
        pingList.add(ping.id.toString())
        pingList.add(ping.title)
        pingList.add(ping.body.orEmpty())
        pingList.add(ping.pic.orEmpty())
        //pingList.add(ping.link.orEmpty())
        //pingList.add(ping.audio.orEmpty()) // Not yet supported
        return pingList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val pingListFieldList = mutableListOf<String>()
        pingListFieldList.add("id")
        pingListFieldList.add("title")
        pingListFieldList.add("body")
        pingListFieldList.add("link")
        //pingListFieldList.add("pic")
        //pingListFieldList.add("audio") // Not yet supported
        return pingListFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Ping {
        return Ping(
            id = fields[0].toLong(),
            title = fields[1],
            body = fields.getOrNull(2)?.takeIf { it.isNotBlank() },
            pic = fields.getOrNull(4)?.takeIf { it.isNotBlank() },
            //link = fields.getOrNull(3)?.takeIf { it.isNotBlank() },
            //audio = fields.getOrNull(5)?.takeIf { it.isNotBlank() }, // Not yet supported
        )
    }
}