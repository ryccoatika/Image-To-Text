package com.ryccoatika.imagetotext.core.utils

import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.core.model.TextScanned

fun TextScanned.toTextScannedEntity(): TextScannedEntity =
    TextScannedEntity(
        dateTime = dateTime,
        text = text
    )

fun TextScannedEntity.toTextScannedDomain(): TextScanned =
    TextScanned(
        dateTime = dateTime,
        text = text
    )