package com.ryccoatika.imagetotext.core.data.local.entity

import android.net.Uri
import android.util.Size
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ryccoatika.imagetotext.domain.model.TextRecognized

@Entity
data class TextScannedEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var imageUri: Uri,
    var imageSize: Size,
    var text: String,
    val textRecognized: TextRecognized,
)
