package edu.ucne.registro_prioridades.data.remote

import androidx.room.Delete
import edu.ucne.registro_prioridades.data.remote.dto.PrioridadDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PrioridadApi {
    @GET("api/Prioridades")
    suspend fun getAll(): List<PrioridadDto>

    @GET("api/Prioridades/{PrioridadId}")
    suspend fun getPrioridad(@Path("PrioridadId") prioridadId: Int): PrioridadDto

    @POST ("api/Prioridades")
    suspend fun save (@Body prioridadDto: PrioridadDto?): PrioridadDto

    @DELETE ("api/Prioridades/{PrioridadId}")
    suspend fun delete (@Path("PrioridadId") prioridadId: Int)

}