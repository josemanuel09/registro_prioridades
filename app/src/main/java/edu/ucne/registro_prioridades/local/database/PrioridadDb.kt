package  edu.ucne.registro_prioridades.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import  edu.ucne.registro_prioridades.local.dao.PrioridadDao
import edu.ucne.registro_prioridades.local.entities.PrioridadEntity


@Database(
    entities = [
        PrioridadEntity::class
    ],

    version = 1,
    exportSchema = false
)
abstract class PrioridadDb : RoomDatabase() {
    abstract fun prioridadDao() : PrioridadDao
}