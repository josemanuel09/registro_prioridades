package edu.ucne.registro_prioridades.presentation.navigation

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

    @Serializable
    data object TicketsList : ScreenPrioridades()

    @Serializable
    data class Tickets(val ticketId: Int) : ScreenPrioridades()

    @Serializable
    data class EditTicket(val ticketId: Int) : ScreenPrioridades()

    @Serializable
    data class DeleteTicket(val ticketId: Int) : ScreenPrioridades()
    @Serializable
    data object HomeScreen : ScreenPrioridades()



}
