package com.ryccoatika.imagetotext.core.utils

import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.domain.model.TextScanned

fun com.ryccoatika.imagetotext.domain.model.TextScanned.toTextScannedEntity(): TextScannedEntity =
    TextScannedEntity(
        dateTime = dateTime,
        text = text
    )

fun TextScannedEntity.toTextScannedDomain(): com.ryccoatika.imagetotext.domain.model.TextScanned =
    com.ryccoatika.imagetotext.domain.model.TextScanned(
        dateTime = dateTime,
        text = text
    )