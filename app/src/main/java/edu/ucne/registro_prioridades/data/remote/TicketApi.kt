package edu.ucne.registro_prioridades.data.remote

import edu.ucne.registro_prioridades.data.remote.dto.TicketDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApi {
    @GET("api/Tickets")
    suspend fun getAll(): List<TicketDto>
    @GET("api/Tickets/{TicketId}")
    suspend fun getTicket(@Path("TicketId") ticketId: Int): TicketDto
    @POST("api/Tickets")
    suspend fun save (@Body ticketDto: TicketDto?): TicketDto
    @DELETE("api/Tickets/{TicketId}")
    suspend fun delete (@Path("TicketId") ticketId: Int)


}