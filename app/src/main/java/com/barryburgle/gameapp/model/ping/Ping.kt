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
    @ColumnInfo(name = "link") var link: String? = null,
    @ColumnInfo(name = "pic") var pic: String? = null,
    @ColumnInfo(name = "audio") var audio: String? = null, // TODO: implement audio acquisition in highest quality
) {
    constructor() : this(0, "", null, null, null, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ping) return false

        return id == other.id &&
                title == other.title &&
                body == other.body &&
                link == other.link &&
                pic == other.pic &&
                audio == other.audio
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (pic?.hashCode() ?: 0)
        result = 31 * result + (audio?.hashCode() ?: 0)
        return result
    }

    fun getShareableText() = buildString {
        if (!body.isNullOrBlank()) {
            append(body)
        }
        if (!link.isNullOrBlank()) {
            append("\n").append(link)
        }
    }

    fun getClipData(context: Context): ClipData {
        val shareableText = getShareableText()
        val imageUri =
            pic?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
        val clipData = if (imageUri != null) {
            ClipData.newUri(context.contentResolver, title, imageUri)
                .apply {
                    if (shareableText.isNotBlank()) {
                        addItem(ClipData.Item(shareableText))
                    }
                }
        } else {
            ClipData.newPlainText(title, shareableText)
        }
        return clipData
    }
}