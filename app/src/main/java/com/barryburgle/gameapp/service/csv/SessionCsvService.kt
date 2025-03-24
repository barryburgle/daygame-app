package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.batch.BatchSessionService

class SessionCsvService : AbstractCsvService<AbstractSession>() {

    companion object {
        private const val SESSIONS_BACKUP_FILENAME: String = "sessions_backup"
    }

    public override fun getBackupFileName(): String {
        return SESSIONS_BACKUP_FILENAME
    }

    val batchSessionService = BatchSessionService()

    override fun exportSingleRow(abstractSession: AbstractSession): Array<String> {
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

    override fun generateHeader(): Array<String> {
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

    override fun mapImportRow(fields: Array<String>): AbstractSession {
        return batchSessionService.init(
            fields[0],
            fields[2].substring(0, 10),
            fields[3].substring(11, 16),
            fields[4].substring(11, 16),
            fields[5],
            fields[6],
            fields[7],
            fields[8]
        )
    }

    override fun isEntityValid(session: AbstractSession): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (session.id == 0L || session.id == null || session.insertTime.isEmpty()) {
            return false
        }
        return true
    }
}