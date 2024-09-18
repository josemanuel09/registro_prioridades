package edu.ucne.registro_prioridades.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registro_prioridades.data.local.entities.PrioridadEntity
import edu.ucne.registro_prioridades.data.local.entities.TicketEntity
import edu.ucne.registro_prioridades.data.repository.PrioridadRepository
import edu.ucne.registro_prioridades.data.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val PrioridadRepository: PrioridadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getTickets()
        getPrioridades()
    }

    fun saveTicket() {
        viewModelScope.launch {
            if (_uiState.value.cliente.isBlank() || _uiState.value.asunto.isBlank() || _uiState.value.prioridadId == null || _uiState.value.fecha.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Todos los campos son obligatorios.")
                }
                return@launch
            }

            try {
                ticketRepository.save(_uiState.value.toEntity())
                nuevoTicket()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al guardar el ticket: ${e.message}")
                }
            }
        }
    }

    fun nuevoTicket() {
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = "",
                prioridadId = null,
                cliente = "",
                asunto = "",
                descripcion = "",
                errorMessage = null
            )
        }
    }

    fun selectTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha ?: "",
                        prioridadId = ticket?.prioridadId,
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: ""
                    )
                }
            }
        }
    }

    fun deleteTicket() {
        viewModelScope.launch {
            try {
                ticketRepository.delete(_uiState.value.toEntity())
                nuevoTicket()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al eliminar el ticket: ${e.message}")
                }
            }
        }
    }

    fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getTickets().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }
    fun getPrioridades(){
        viewModelScope.launch {
            PrioridadRepository.getPrioridades().collect{prioridades->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }

    }

    fun onFechaChange(fecha: String) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    fun onPrioridadIdChange(prioridadId: Int?) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    data class UiState(
        val ticketId: Int? = null,
        val fecha: String = "",
        val prioridadId: Int? = null,
        val cliente: String = "",
        val asunto: String = "",
        val descripcion: String = "",
        val errorMessage: String? = null,
        val tickets: List<TicketEntity> = emptyList(),
        val prioridades: List<PrioridadEntity> = emptyList()
    )

    fun UiState.toEntity() = TicketEntity(
        ticketId = ticketId,
        fecha = fecha,
        prioridadId = prioridadId ?: 1,
        cliente = cliente,
        asunto = asunto,
        descripcion = descripcion
    )
}
