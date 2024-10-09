package edu.ucne.registro_prioridades.data.repository

import edu.ucne.registro_prioridades.data.remote.SistemasApi
import edu.ucne.registro_prioridades.data.remote.dto.SistemasDto
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val sistemaApi: SistemasApi
){
    suspend fun save (sistema: SistemasDto) = sistemaApi.save(sistema)
    suspend fun getSistema(id: Int) = sistemaApi.getSistema(id)
    suspend fun delete(id: Int) = sistemaApi.delete(id)
    suspend fun getAll() = sistemaApi.getAll()
}
