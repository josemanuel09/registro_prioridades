package edu.ucne.registro_prioridades.data.remote

import edu.ucne.registro_prioridades.data.remote.dto.SistemasDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SistemasApi {
    @GET("api/Sistemas")
    suspend fun getAll(): List<SistemasDto>
    @GET("api/Sistemas/{SistemaId}")
    suspend fun getSistema(@Path("SistemaId") sistemaId: Int): SistemasDto
    @POST("api/Sistemas")
    suspend fun save (@Body sistemaDto: SistemasDto?): SistemasDto
    @DELETE("api/Sistemas/{SistemaId}")
    suspend fun delete (@Path("SistemaId") sistemaId: Int)


}