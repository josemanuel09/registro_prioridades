package edu.ucne.registro_prioridades.data.repository

import edu.ucne.registro_prioridades.data.local.dao.TicketDao
import edu.ucne.registro_prioridades.data.local.entities.TicketEntity
import edu.ucne.registro_prioridades.data.remote.TicketApi
import edu.ucne.registro_prioridades.data.remote.dto.TicketDto
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketApi: TicketApi
) {
    suspend fun save(ticket: TicketDto) = ticketApi.save(ticket)

    suspend fun getTicket(id: Int) = ticketApi.getTicket(id)

    suspend fun delete(id: Int) = ticketApi.delete(id)

   suspend fun getTickets() = ticketApi.getAll()
}
