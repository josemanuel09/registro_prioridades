package edu.ucne.registro_prioridades.data.remote.dto

data class TicketDto(
    val ticketId: Int,
    val fecha: String,
    val clienteId: Int,
    val sistemaId: Int,
    val prioridadId: Int,
    val solicitadoPor: String,
    val asunto: String,
    val descripcion: String
)
