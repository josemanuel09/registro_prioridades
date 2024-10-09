package edu.ucne.registro_prioridades.data.repository

import edu.ucne.registro_prioridades.data.local.dao.PrioridadDao
import edu.ucne.registro_prioridades.data.local.entities.PrioridadEntity
import edu.ucne.registro_prioridades.data.remote.PrioridadApi
import edu.ucne.registro_prioridades.data.remote.dto.PrioridadDto
import javax.inject.Inject


class PrioridadRepository @Inject constructor(
    private val prioridadApi: PrioridadApi
) {
    suspend fun save(prioridad: PrioridadDto) = prioridadApi.save(prioridad)

    suspend fun getPrioridad(id: Int) = prioridadApi.getPrioridad(id)

    suspend fun delete(prioridad: PrioridadDto) = prioridadApi.delete(prioridad.prioridadId ?: 0)

    suspend fun getAll() = prioridadApi.getAll()
}
