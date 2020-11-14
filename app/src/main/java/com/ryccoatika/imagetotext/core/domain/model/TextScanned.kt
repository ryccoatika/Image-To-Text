package com.ryccoatika.imagetotext.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TextScanned(
    val dateTime: Long,
    var text: String
): Parcelable