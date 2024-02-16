package com.ryccoatika.imagetotext.core.data.local.room.typeconverter

import android.util.Size
import androidx.room.TypeConverter

class SizeTypeConverter {
    @TypeConverter
    fun fromString(value: String) = Size.parseSize(value)

    @TypeConverter
    fun toString(value: Size) = value.toString()
}
