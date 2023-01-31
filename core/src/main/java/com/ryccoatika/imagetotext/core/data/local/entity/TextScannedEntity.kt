package com.ryccoatika.imagetotext.core.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ryccoatika.imagetotext.domain.model.TextRecognized

@Entity
data class TextScannedEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var image: Bitmap,
    var text: String,
    val textRecognized: TextRecognized
)