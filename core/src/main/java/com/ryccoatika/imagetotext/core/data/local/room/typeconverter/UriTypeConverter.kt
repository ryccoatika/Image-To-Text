package com.ryccoatika.imagetotext.core.data.local.room.typeconverter

import android.net.Uri
import androidx.room.TypeConverter

class UriTypeConverter {
    @TypeConverter
    fun fromString(value: String) = Uri.parse(value)

    @TypeConverter
    fun toString(uri: Uri) = uri.toString()
}
