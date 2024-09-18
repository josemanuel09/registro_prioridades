package edu.ucne.registro_prioridades.data.repository

import edu.ucne.registro_prioridades.data.local.dao.TicketDao
import edu.ucne.registro_prioridades.data.local.entities.TicketEntity
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = ticketDao.Save(ticket)

    suspend fun getTicket(id: Int) = ticketDao.find(id)

    suspend fun delete(ticket: TicketEntity) = ticketDao.delete(ticket)

    fun getTickets() = ticketDao.getAll()
}
