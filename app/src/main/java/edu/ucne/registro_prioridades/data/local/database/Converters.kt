package edu.ucne.registro_prioridades.data.local.database

import androidx.room.TypeConverters
import java.util.Date

class Converters {
    @TypeConverters
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }

    }

    @TypeConverters
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}