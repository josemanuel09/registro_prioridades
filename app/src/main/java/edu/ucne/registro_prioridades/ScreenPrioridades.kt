package edu.ucne.registro_prioridades

import kotlinx.serialization.Serializable

sealed class ScreenPrioridades {
    @Serializable
    data object PrioridadesList : ScreenPrioridades()

    @Serializable
    data class Prioridades(val prioridadId: Int) : ScreenPrioridades()

    @Serializable
    data class EditPrioridad(val prioridadId: Int) : ScreenPrioridades()

    @Serializable
    data class DeletePrioridad(val prioridadId: Int) : ScreenPrioridades()

}
