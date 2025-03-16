package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.date.Date

class DateCsvService : AbstractCsvService<Date>() {

    override fun exportSingleRow(date: Date): Array<String> {
        val dateFieldList = mutableListOf<String>()
        dateFieldList.add(date.id.toString())
        dateFieldList.add(date.insertTime)
        dateFieldList.add(date.leadId.toString())
        dateFieldList.add(cleaEmptyField(date.location))
        dateFieldList.add(cleaEmptyField(date.date))
        dateFieldList.add(date.startHour)
        dateFieldList.add(date.endHour)
        dateFieldList.add(date.cost.toString())
        dateFieldList.add(date.dateNumber.toString())
        dateFieldList.add(date.dateType)
        dateFieldList.add(date.pull.toString())
        dateFieldList.add(date.bounce.toString())
        dateFieldList.add(date.kiss.toString())
        dateFieldList.add(date.lay.toString())
        dateFieldList.add(date.recorded.toString())
        dateFieldList.add(cleaEmptyField(date.stickingPoints))
        dateFieldList.add(cleaEmptyField(date.tweetUrl))
        dateFieldList.add(date.dateTime.toString())
        dateFieldList.add(date.dayOfWeek.toString())
        dateFieldList.add(date.weekNumber.toString())
        return dateFieldList.toTypedArray()
    }


    override fun generateHeader(): Array<String> {
        val dateFieldList = mutableListOf<String>()
        dateFieldList.add("id")
        dateFieldList.add("insert_time")
        dateFieldList.add("lead_id")
        dateFieldList.add("location")
        dateFieldList.add("meeting_date")
        dateFieldList.add("start_hour")
        dateFieldList.add("end_hour")
        dateFieldList.add("cost")
        dateFieldList.add("date_number")
        dateFieldList.add("date_type")
        dateFieldList.add("pull")
        dateFieldList.add("bounce")
        dateFieldList.add("kiss")
        dateFieldList.add("lay")
        dateFieldList.add("recorded")
        dateFieldList.add("sticking_points")
        dateFieldList.add("tweet_url")
        dateFieldList.add("date_time")
        dateFieldList.add("day_of_week")
        dateFieldList.add("week_number")
        return dateFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Date {
        return Date(
            importLong(fields[0]),
            fields[1],
            importLong(fields[2]),
            fields[3],
            fields[4],
            fields[5],
            fields[6],
            fields[7].toInt(),
            fields[8].toInt(),
            fields[9],
            fields[10].toBoolean(),
            fields[11].toBoolean(),
            fields[12].toBoolean(),
            fields[13].toBoolean(),
            fields[14].toBoolean(),
            fields[15],
            fields[16],
            importLong(fields[17])!!,
            fields[18].toInt(),
            fields[19].toInt(),
        )
    }
}