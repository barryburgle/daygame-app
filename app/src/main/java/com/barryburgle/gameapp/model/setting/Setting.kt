package com.barryburgle.gameapp.model.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting")
open class Setting(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "value") var value: String
) {

    /*InsertTime-agnostic equals*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Setting) return false

        return id == other.id &&
                value == other.value
    }

    /*InsertTime-agnostic hashCode*/
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

}