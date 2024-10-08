package  edu.ucne.registro_prioridades.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import  edu.ucne.registro_prioridades.data.local.dao.PrioridadDao
import edu.ucne.registro_prioridades.data.local.dao.TicketDao
import edu.ucne.registro_prioridades.data.local.entities.PrioridadEntity
import edu.ucne.registro_prioridades.data.local.entities.TicketEntity


@Database(
    entities = [
        PrioridadEntity::class,
        TicketEntity::class
    ],

    version = 3,
    exportSchema = false
)
abstract class PrioridadDb : RoomDatabase() {
    abstract fun prioridadDao() : PrioridadDao
    abstract fun ticketDao() : TicketDao

}