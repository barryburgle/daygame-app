package com.barryburgle.gameapp.model.ping

import android.content.ClipData
import android.content.Context
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ping")
open class Ping(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "body") var body: String? = null,
    @ColumnInfo(name = "web_url") var webUrl: String? = null,
    @ColumnInfo(name = "local_media_uri") var localMediaUri: String? = null,
) {
    constructor() : this(0, "", null, null, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ping) return false

        return id == other.id &&
                title == other.title &&
                body == other.body &&
                webUrl == other.webUrl &&
                localMediaUri == other.localMediaUri
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (webUrl?.hashCode() ?: 0)
        result = 31 * result + (localMediaUri?.hashCode() ?: 0)
        return result
    }
}