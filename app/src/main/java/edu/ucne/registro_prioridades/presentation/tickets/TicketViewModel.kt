package edu.ucne.registro_prioridades.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registro_prioridades.data.remote.dto.ClienteDto
import edu.ucne.registro_prioridades.data.remote.dto.PrioridadDto
import edu.ucne.registro_prioridades.data.remote.dto.SistemasDto
import edu.ucne.registro_prioridades.data.remote.dto.TicketDto
import edu.ucne.registro_prioridades.data.repository.ClienteRepository
import edu.ucne.registro_prioridades.data.repository.PrioridadRepository
import edu.ucne.registro_prioridades.data.repository.SistemaRepository
import edu.ucne.registro_prioridades.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val clienteRepository: ClienteRepository,
    private val sistemaRepository: SistemaRepository,
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getTickets()
        getClientes()
        getSistemas()
        getPrioridades()
    }

    fun saveTicket() {
        viewModelScope.launch {
            if ( _uiState.value.sistemaId == null ||_uiState.value.clienteId == null || _uiState.value.asunto.isBlank() ||_uiState.value.solicitadoPor.isBlank()|| _uiState.value.descripcion.isBlank() || _uiState.value.prioridadId == null || _uiState.value.sistemaId == null || _uiState.value.fecha == null) {
                _uiState.update { it.copy(errorMessage = "Todos los campos son obligatorios.") }
                return@launch
            }

            try {
                ticketRepository.save(_uiState.value.toEntity())
                nuevo()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al guardar el ticket: ${e.message}") }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                ticketId = 0,
                fecha = Date(),
                prioridadId = null,
                clienteId = null,
                sistemaId = null,
                solicitadoPor = "",
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
                ticket?.let {
                    _uiState.update {
                        it.copy(
                            ticketId = ticket.ticketId,
                            fecha = ticket.fecha,
                            prioridadId = ticket.prioridadId,
                            clienteId = ticket.clienteId,
                            solicitadoPor = ticket.solicitadoPor,
                            sistemaId = ticket.sistemaId,
                            asunto = ticket.asunto,
                            descripcion = ticket.descripcion
                        )
                    }
                }
            }
        }
    }

    fun onClienteChange(ClienteId: Int){
        _uiState.update { it.copy(clienteId = ClienteId) }
    }
    fun onAsuntoChange(asunto: String){
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }
    fun delete() {
        viewModelScope.launch {
            ticketRepository.delete(_uiState.value.ticketId!!)
            nuevo()
        }
    }
    fun onDescripcionChange(descripcion: String){
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onPrioridadChange(prioridadId: Int){
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }
    fun onSistemaChange(sistemaId: Int){
        _uiState.update {
            it.copy(sistemaId = sistemaId)
        }
    }
    fun onFechaChange(fecha: Date){
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }
    fun onSolicitadoPorChange(solicitadoPor: String){
        _uiState.update {
            it.copy(solicitadoPor = solicitadoPor)
        }
    }




    private fun getTickets() {
        viewModelScope.launch {
            val tickets = ticketRepository.getTickets()
            _uiState.update { it.copy(tickets = tickets) }
        }
    }

    private fun getClientes() {
        viewModelScope.launch {
            try {
                val clientes = clienteRepository.getClientes()
                _uiState.update { it.copy(clientes = clientes) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar clientes: ${e.message}") }
            }
        }
    }

    private fun getSistemas() {
        viewModelScope.launch {
            try {
                val sistemas = sistemaRepository.getAll()
                _uiState.update { it.copy(sistemas = sistemas) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar sistemas: ${e.message}") }
            }
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            try {
                val prioridades = prioridadRepository.getAll()
                _uiState.update { it.copy(prioridades = prioridades) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar prioridades: ${e.message}") }
            }
        }
    }

    data class UiState(
        val ticketId: Int = 0,
        val fecha: Date = Date(),
        val prioridadId: Int? = null,
        val clienteId: Int? = null,
        val sistemaId: Int? = null,
        val solicitadoPor: String = "",
        val asunto: String = "",
        val descripcion: String = "",
        val errorMessage: String? = null,
        val tickets: List<TicketDto> = emptyList(),
        val clientes: List<ClienteDto> = emptyList(),
        val sistemas: List<SistemasDto> = emptyList(),
        val prioridades: List<PrioridadDto> = emptyList()
    )

    fun UiState.toEntity() = TicketDto(
        ticketId = ticketId,
        fecha = fecha,
        prioridadId = prioridadId ?: 0,
        clienteId = clienteId ?: 0,
        asunto = asunto,
        sistemaId = sistemaId ?: 0,
        solicitadoPor = solicitadoPor,
        descripcion = descripcion
    )
}
