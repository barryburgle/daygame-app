package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.ping.SentPing

class SentPingCsvService : AbstractCsvService<SentPing>() {

    companion object {
        private const val SENT_PINGS_BACKUP_FILENAME: String = "sent_pings_backup"
    }

    override fun getBackupFileName(): String {
        return SENT_PINGS_BACKUP_FILENAME
    }

    override fun exportSingleRow(sentPing: SentPing): Array<String> {
        val sentPingList = mutableListOf<String>()
        sentPingList.add(sentPing.id.toString())
        sentPingList.add(sentPing.pingId.toString())
        sentPingList.add(sentPing.leadId.toString())
        sentPingList.add(sentPing.sentHour)
        sentPingList.add(cleaEmptyField(sentPing.reaction))
        return sentPingList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val sentPingListFieldList = mutableListOf<String>()
        sentPingListFieldList.add("id")
        sentPingListFieldList.add("ping_id")
        sentPingListFieldList.add("lead_id")
        sentPingListFieldList.add("sent_hour")
        sentPingListFieldList.add("reaction")
        return sentPingListFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): SentPing {
        return SentPing(
            id = fields[0].toLong(),
            pingId = fields[1].toLong(),
            leadId = fields[2].toLong(),
            sentHour = fields[3],
            reaction = fields[4]
        )
    }
}