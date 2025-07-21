
package com.jmr.medhealth.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // This is the TypeConverter to fix your "Cannot figure out how to save this field" error.
    // It converts a List of Strings to a JSON string for storage in the database.
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    // This converts the JSON string back to a List of Strings when retrieving from the database.
    @TypeConverter
    fun toStringList(jsonString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}