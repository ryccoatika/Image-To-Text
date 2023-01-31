package com.ryccoatika.imagetotext.core.data.local.room.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ryccoatika.imagetotext.domain.model.TextRecognized

class TextRecognizedTypeConverter {
    @TypeConverter
    fun fromString(value: String) = Gson().fromJson(value, TextRecognized::class.java)

    @TypeConverter
    fun toString(textRecognized: TextRecognized) = Gson().toJson(textRecognized)
}