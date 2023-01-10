package com.ryccoatika.imagetotext.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TextScannedEntity (
    @PrimaryKey
    var dateTime: Long,
    var text: String
)