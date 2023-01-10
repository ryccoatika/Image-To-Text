package com.ryccoatika.imagetotext.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TextScanned(
    val dateTime: Long,
    var text: String
): Parcelable