package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.lead.Lead

class LeadCsvService : AbstractCsvService<Lead>() {

    companion object {
        private const val LEADS_BACKUP_FILENAME: String = "leads_backup"
    }

    public override fun getBackupFileName(): String {
        return LEADS_BACKUP_FILENAME
    }

    override fun exportSingleRow(lead: Lead): Array<String> {
        val leadFieldList = mutableListOf<String>()
        leadFieldList.add(lead.id.toString())
        leadFieldList.add(lead.insertTime)
        leadFieldList.add(lead.sessionId.toString())
        leadFieldList.add(lead.name)
        leadFieldList.add(lead.contact)
        leadFieldList.add(lead.nationality)
        leadFieldList.add(lead.age.toString())
        leadFieldList.add(lead.contactLookupKey ?: "")
        leadFieldList.add(lead.instagramUrl ?: "")
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
        leadFieldList.add("contact_lookup_key")
        leadFieldList.add("instagram_url")
        return leadFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Lead {
        val id = importLong(fields[0])
        val age = importLong(fields[6])
        return Lead(
            if (id != null) id else 0L,
            fields[1],
            importLong(fields[2]),
            fields[3],
            fields[4],
            fields[5],
            if (age != null) age else 20L,
            if (fields.size > 7) fields[7].ifEmpty { null } else null,
            if (fields.size > 8) fields[8].ifEmpty { null } else null
        )
    }

    override fun isEntityValid(lead: Lead): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (lead.id == 0L || lead.id == null || lead.insertTime.isEmpty()) {
            return false
        }
        return true
    }
}