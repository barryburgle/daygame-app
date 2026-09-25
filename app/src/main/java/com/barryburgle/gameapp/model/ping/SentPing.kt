package com.barryburgle.gameapp.model.ping

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sent_ping")
open class SentPing(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ping_id") var pingId: Long,
    @ColumnInfo(name = "lead_id") var leadId: Long,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "reaction") var reaction: String? // This column should contain value for an enum meaning ["no_response", "ok_response", "warm_response"]
) {
    constructor() : this(0, 0, 0, "", "")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SentPing) return false

        return id == other.id && pingId == other.pingId && leadId == other.leadId && insertTime == other.insertTime && reaction == other.reaction
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + pingId.hashCode()
        result = 31 * result + leadId.hashCode()
        result = 31 * result + insertTime.hashCode()
        result = 31 * result + reaction.hashCode()
        return result
    }
}