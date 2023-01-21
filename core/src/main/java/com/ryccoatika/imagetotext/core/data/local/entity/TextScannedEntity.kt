package com.ryccoatika.imagetotext.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ryccoatika.imagetotext.domain.model.TextRecognized

@Entity
data class TextScannedEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var imageUri: String,
    var text: String,
    val textRecognized: TextRecognized
)