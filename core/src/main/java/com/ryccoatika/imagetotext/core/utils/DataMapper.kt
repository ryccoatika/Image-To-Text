package com.ryccoatika.imagetotext.core.utils

import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned

fun TextScanned.toTextScannedEntity(): TextScannedEntity {
    return TextScannedEntity(
        id = id,
        text = text,
        image = image,
        textRecognized = textRecognized
    )
}

fun TextScannedEntity.toTextScannedDomain(): TextScanned =
    TextScanned(
        id = id ?: 0,
        text = text,
        image = image,
        textRecognized = textRecognized
    )

fun Text.toTextRecognizedDomain(): TextRecognized = TextRecognized(
    text = text,
    textBlocks = textBlocks.map { textBlock ->
        TextRecognized.TextBlock(
            text = textBlock.text,
            lines = textBlock.lines.map { line ->
                TextRecognized.Line(
                    text = line.text,
                    elements = line.elements.map { element ->
                        TextRecognized.Element(
                            text = element.text,
                            angle = element.angle,
                            boundingBox = element.boundingBox
                        )
                    },
                )
            },
            boundingBox = textBlock.boundingBox
        )
    }
)