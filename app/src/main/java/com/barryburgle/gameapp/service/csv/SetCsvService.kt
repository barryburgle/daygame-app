package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.set.SetService

class SetCsvService : AbstractCsvService<SingleSet>() {

    companion object {
        private const val SETS_BACKUP_FILENAME: String = "sets_backup"
    }

    public override fun getBackupFileName(): String {
        return SETS_BACKUP_FILENAME
    }

    val setService = SetService()

    override fun exportSingleRow(set: SingleSet): Array<String> {
        val setFieldList = mutableListOf<String>()
        setFieldList.add(set.id.toString())
        setFieldList.add(set.insertTime)
        setFieldList.add(set.date)
        setFieldList.add(set.startHour)
        setFieldList.add(set.endHour)
        setFieldList.add(set.sessionId.toString())
        setFieldList.add(cleaEmptyField(set.location))
        setFieldList.add(set.conversation.toString())
        setFieldList.add(set.contact.toString())
        setFieldList.add(set.instantDate.toString())
        setFieldList.add(set.recorded.toString())
        setFieldList.add(set.leadId.toString())
        setFieldList.add(set.dateId.toString())
        setFieldList.add(cleaEmptyField(set.stickingPoints))
        setFieldList.add(cleaEmptyField(set.tweetUrl))
        setFieldList.add(set.setTime.toString())
        setFieldList.add(set.dayOfWeek.toString())
        setFieldList.add(set.weekNumber.toString())
        return setFieldList.toTypedArray()
    }


    override fun generateHeader(): Array<String> {
        val leadFieldList = mutableListOf<String>()
        leadFieldList.add("id")
        leadFieldList.add("insert_time")
        leadFieldList.add("date")
        leadFieldList.add("start_hour")
        leadFieldList.add("end_hour")
        leadFieldList.add("session_id")
        leadFieldList.add("location")
        leadFieldList.add("conversation")
        leadFieldList.add("contact")
        leadFieldList.add("instant_date")
        leadFieldList.add("recorded")
        leadFieldList.add("lead_id")
        leadFieldList.add("date_id")
        leadFieldList.add("sticking_points")
        leadFieldList.add("tweet_url")
        leadFieldList.add("set_time")
        leadFieldList.add("day_of_week")
        leadFieldList.add("week_number")
        return leadFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): SingleSet {
        return setService.init(
            fields[0],
            fields[2],
            fields[3],
            fields[4],
            importLong(fields[5]),
            fields[6],
            fields[7].toBoolean(),
            fields[8].toBoolean(),
            fields[9].toBoolean(),
            fields[10].toBoolean(),
            importLong(fields[11]),
            importLong(fields[12]),
            fields[13],
            fields[14]
        )
    }

    override fun isEntityValid(set: SingleSet): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (set.id == 0L || set.id == null || set.insertTime.isEmpty()) {
            return false
        }
        return true
    }
}