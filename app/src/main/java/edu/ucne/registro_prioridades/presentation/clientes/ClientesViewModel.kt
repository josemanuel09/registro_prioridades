package edu.ucne.prioridadregistro.presentation.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registro_prioridades.data.remote.dto.ClienteDto
import edu.ucne.registro_prioridades.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getClientes()
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            when {
                state.nombres.isBlank() -> {
                    _uiState.update {
                        it.copy(errorMessage = "El nombre no puede estar vacío", successMessage = null)
                    }
                }
                state.telefono.isBlank() -> {
                    _uiState.update {
                        it.copy(errorMessage = "El teléfono no puede estar vacío", successMessage = null)
                    }
                }
                else -> {
                    try {
                        clienteRepository.saveCliente(state.toEntity())
                        _uiState.update {
                            it.copy(
                                successMessage = "Cliente guardado exitosamente",
                                errorMessage = null
                            )
                        }
                        nuevo()
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(errorMessage = "Error al guardar el cliente", successMessage = null)
                        }
                    }
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                clienteId = null,
                nombres = "",
                telefono = "",
                celular = "",
                rnc = "",
                email = "",
                direccion = "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun select(clienteId: Int) {
        viewModelScope.launch {
            try {
                if (clienteId > 0) {
                    val cliente = clienteRepository.getClienteById(clienteId)
                    _uiState.update {
                        it.copy(
                            clienteId = cliente?.clienteId,
                            nombres = cliente?.nombres ?: "",
                            telefono = cliente?.telefono ?: "",
                            celular = cliente?.celular ?: "",
                            rnc = cliente?.rnc ?: "",
                            email = cliente?.email ?: "",
                            direccion = cliente?.direccion ?: "",
                            errorMessage = null,
                            successMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al cargar el cliente", successMessage = null)
                }
            }
        }
    }

    fun delete(clienteId: Int) {
        viewModelScope.launch {
            try {
                clienteRepository.delete(clienteId)
                _uiState.update {
                    it.copy(
                        successMessage = "Cliente eliminado exitosamente",
                        errorMessage = null
                    )
                }
                nuevo()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al eliminar el cliente", successMessage = null)
                }
            }
        }
    }

    private fun getClientes() {
        viewModelScope.launch {
            try {
                val clientes = clienteRepository.getClientes()
                if (clientes.isEmpty()) {
                    throw EmptyListException("La lista de clientes está vacía")
                }
                _uiState.update { it.copy(clientes = clientes) }
            } catch (e: EmptyListException) {
                _uiState.update { it.copy(errorMessage = e.message, successMessage = null) }
            }
        }
    }

    class EmptyListException(message: String) : Exception(message)

    // Actualizaciones de UI para cambios en el formulario
    fun onNombresChange(nombres: String) {
        _uiState.update {
            it.copy(nombres = nombres, errorMessage = null, successMessage = null)
        }
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(telefono = telefono, errorMessage = null, successMessage = null)
        }
    }

    fun onCelularChange(celular: String) {
        _uiState.update {
            it.copy(celular = celular, errorMessage = null, successMessage = null)
        }
    }

    fun onRncChange(rnc: String) {
        _uiState.update {
            it.copy(rnc = rnc, errorMessage = null, successMessage = null)
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, errorMessage = null, successMessage = null)
        }
    }

    fun onDireccionChange(direccion: String) {
        _uiState.update {
            it.copy(direccion = direccion, errorMessage = null, successMessage = null)
        }
    }

    data class UiState(
        val clienteId: Int? = null,
        val nombres: String = "",
        val telefono: String = "",
        val celular: String = "",
        val rnc: String = "",
        val email: String = "",
        val direccion: String = "",
        val errorMessage: String? = null,
        val successMessage: String? = null,
        val clientes: List<ClienteDto> = emptyList()
    )

    fun UiState.toEntity() = ClienteDto(
        clienteId = clienteId,
        nombres = nombres,
        telefono = telefono,
        celular = celular,
        rnc = rnc,
        email = email,
        direccion = direccion
    )
}
