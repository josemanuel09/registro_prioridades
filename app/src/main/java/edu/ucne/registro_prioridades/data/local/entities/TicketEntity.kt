package edu.ucne.registro_prioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Tickets",
    foreignKeys = [
        ForeignKey(
            entity = PrioridadEntity::class,
            parentColumns = ["prioridadId"],
            childColumns = ["prioridadId"]
        )

    ]
)
data class TicketEntity (
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = ""


)