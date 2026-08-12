package com.barryburgle.gameapp.model.lead

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lead")
open class Lead(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "insert_time") var insertTime: String,
    @ColumnInfo(name = "session_id") var sessionId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "contact") var contact: String,
    @ColumnInfo(name = "nationality") var nationality: String,
    @ColumnInfo(name = "age") var age: Long,
    @ColumnInfo(name = "contact_lookup_key") var contactLookupKey: String? = null,
    @ColumnInfo(name = "instagram_url") var instagramUrl: String? = null,
    @ColumnInfo(name = "pinpoint_id") var pinPointId: Long? = null,
) {

    /*InsertTime-agnostic equals*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Lead) return false

        return id == other.id &&
                sessionId == other.sessionId &&
                name == other.name &&
                contact == other.contact &&
                nationality == other.nationality &&
                age == other.age &&
                contactLookupKey == other.contactLookupKey &&
                instagramUrl == other.instagramUrl &&
                pinPointId == other.pinPointId
    }

    /*InsertTime-agnostic hashCode*/
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (sessionId?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + contact.hashCode()
        result = 31 * result + nationality.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + (contactLookupKey?.hashCode() ?: 0)
        result = 31 * result + (instagramUrl?.hashCode() ?: 0)
        result = 31 * result + (pinPointId?.hashCode() ?: 0)
        return result
    }

    constructor() : this(0, "", null, "", "", "", 20, null)
}
