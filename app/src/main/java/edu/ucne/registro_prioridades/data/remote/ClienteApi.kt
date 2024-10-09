package edu.ucne.registro_prioridades.data.remote

import edu.ucne.registro_prioridades.data.remote.dto.ClienteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClienteApi {
    @GET("/api/Clientes")
    suspend fun getallClientes(): List<ClienteDto>
    @GET("/api/Clientes/{id}")
    suspend fun getClienteById(@Path("id") id: Int): ClienteDto
    @POST("/api/Clientes")
    suspend fun saveCliente(@Body cliente: ClienteDto): ClienteDto
    @DELETE("/api/Clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int)



}