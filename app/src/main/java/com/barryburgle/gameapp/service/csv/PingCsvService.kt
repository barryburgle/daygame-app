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

    override fun exportSingleRow(ping: Ping): Array<String> {
        val pingList = mutableListOf<String>()
        pingList.add(ping.id.toString())
        pingList.add(ping.title)
        pingList.add(ping.body.orEmpty())
        pingList.add(ping.link.orEmpty())
        pingList.add(ping.pic.orEmpty())
        pingList.add(ping.audio.orEmpty())
        pingList.add(ping.leadIds.joinToString(LIST_DELIMITER))
        return pingList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val pingListFieldList = mutableListOf<String>()
        pingListFieldList.add("id")
        pingListFieldList.add("title")
        pingListFieldList.add("body")
        pingListFieldList.add("link")
        pingListFieldList.add("pic")
        pingListFieldList.add("audio")
        pingListFieldList.add("lead_ids")
        return pingListFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Ping {
        val leadIds = if (fields.size > 6 && fields[6].isNotBlank()) {
            fields[6].split(LIST_DELIMITER).mapNotNull { it.trim().toLongOrNull() }
        } else {
            emptyList()
        }

        return Ping(
            id = fields[0].toLong(),
            title = fields[1],
            body = fields.getOrNull(2)?.takeIf { it.isNotBlank() },
            link = fields.getOrNull(3)?.takeIf { it.isNotBlank() },
            pic = fields.getOrNull(4)?.takeIf { it.isNotBlank() },
            audio = fields.getOrNull(5)?.takeIf { it.isNotBlank() },
            leadIds = leadIds
        )
    }
}