package com.ryccoatika.imagetotext.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity (tableName = TextScannedEntity.TABLE_NAME)
@Parcelize
data class TextScannedEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Int? = null,
    @ColumnInfo(name = TEXT)
    var text: String) : Parcelable {
    companion object {
        const val TABLE_NAME = "text_scanned"
        const val ID = "id"
        const val TEXT = "text"
    }
}