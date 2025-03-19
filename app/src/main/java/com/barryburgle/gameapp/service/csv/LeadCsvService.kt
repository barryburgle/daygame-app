package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.lead.Lead

class LeadCsvService : AbstractCsvService<Lead>() {

    override fun exportSingleRow(lead: Lead): Array<String> {
        val leadFieldList = mutableListOf<String>()
        leadFieldList.add(lead.id.toString())
        leadFieldList.add(lead.insertTime)
        leadFieldList.add(lead.sessionId.toString())
        leadFieldList.add(lead.name)
        leadFieldList.add(lead.contact)
        leadFieldList.add(lead.nationality)
        leadFieldList.add(lead.age.toString())
        return leadFieldList.toTypedArray()
    }


    override fun generateHeader(): Array<String> {
        val leadFieldList = mutableListOf<String>()
        leadFieldList.add("id")
        leadFieldList.add("insert_time")
        leadFieldList.add("session_id")
        leadFieldList.add("name")
        leadFieldList.add("contact")
        leadFieldList.add("nationality")
        leadFieldList.add("age")
        return leadFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Lead {
        // TODO: create a init service for lead too
        return Lead(
            fields[0].toLong(),
            fields[1],
            importLong(fields[2]),
            fields[3],
            fields[4],
            fields[5],
            fields[6].toLong()
        )
    }
}