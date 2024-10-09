package edu.ucne.registro_prioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sistemas")
data class SistemaEntity(
    @PrimaryKey
    val sistemaId: Int? = null,
    val nombreDelSistema: String = "",


)