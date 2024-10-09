package edu.ucne.registro_prioridades.data.repository

import edu.ucne.registro_prioridades.data.remote.ClienteApi
import edu.ucne.registro_prioridades.data.remote.dto.ClienteDto
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val clienteApi: ClienteApi
){
    suspend fun getClientes() = clienteApi.getallClientes()
    suspend fun getClienteById(id: Int) = clienteApi.getClienteById(id)
    suspend fun delete(id: Int) = clienteApi.deleteCliente(id)
    suspend fun saveCliente(cliente: ClienteDto) = clienteApi.saveCliente(cliente)

}