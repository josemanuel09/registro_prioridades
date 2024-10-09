package edu.ucne.registro_prioridades.data.remote.dto

data class ClienteDto(
    val clienteId: Int?,
    val nombres: String,
    val telefono: String,
    val celular: String,
    val rnc: String,
    val email: String,
    val direccion: String,

)
