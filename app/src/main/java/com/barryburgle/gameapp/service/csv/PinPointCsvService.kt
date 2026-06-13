package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.session.PinPoint

class PinPointCsvService : AbstractCsvService<PinPoint>() {

    companion object {
        private const val PINPOINTS_BACKUP_FILENAME: String = "pinpoints_backup"
    }

    public override fun getBackupFileName(): String {
        return PINPOINTS_BACKUP_FILENAME
    }

    override fun exportSingleRow(pinPoint: PinPoint): Array<String> {
        val pinPointList = mutableListOf<String>()
        pinPointList.add(pinPoint.id.toString())
        pinPointList.add(pinPoint.sourceEventId.toString())
        pinPointList.add(pinPoint.sourceEventType)
        pinPointList.add(pinPoint.pinPointType)
        pinPointList.add(pinPoint.utcTimestamp)
        pinPointList.add(pinPoint.latitude.toString())
        pinPointList.add(pinPoint.longitude.toString())
        return pinPointList.toTypedArray()
    }

    override fun generateHeader(): Array<String> {
        val pinPointListFieldList = mutableListOf<String>()
        pinPointListFieldList.add("id")
        pinPointListFieldList.add("source_event_id")
        pinPointListFieldList.add("source_event_type")
        pinPointListFieldList.add("pinpoint_type")
        pinPointListFieldList.add("utc_timestamp")
        pinPointListFieldList.add("latitude")
        pinPointListFieldList.add("longitude")
        return pinPointListFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): PinPoint {
        return PinPoint(
            fields[0].toLong(),
            fields[1].toLong(),
            fields[2],
            fields[3],
            fields[4],
            fields[5].toDouble(),
            fields[6].toDouble(),
        )
    }

    override fun isEntityValid(pinPoint: PinPoint): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (pinPoint.id == 0L || pinPoint.id == null || pinPoint.utcTimestamp.isEmpty()) {
            return false
        }
        return true
    }
}