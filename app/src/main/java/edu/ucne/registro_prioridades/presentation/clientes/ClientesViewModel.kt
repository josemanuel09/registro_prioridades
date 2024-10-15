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

    init{
        getClientes()
    }
    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            var hasError = false

            if (state.nombres.isBlank()) {
                _uiState.update { it.copy(nombresError = "El nombre no puede estar vacío") }
                hasError = true
            }
            if (state.telefono.isBlank()) {
                _uiState.update { it.copy(telefonoError = "El teléfono no puede estar vacío") }
                hasError = true
            }

            if (state.celular.isBlank()) {
                _uiState.update { it.copy(celularError = "El celular no puede estar vacío") }
                hasError = true
            }
            if (state.rnc.isBlank()) {
                _uiState.update { it.copy(rncError = "El RNC no puede estar vacío") }
                hasError = true
            }
            if (state.email.isBlank()) {
                _uiState.update { it.copy(emailError = "El email no puede estar vacío") }
                hasError = true
            }
            if (state.direccion.isBlank()) {
                _uiState.update { it.copy(direccionError = "La dirección no puede estar vacía") }
                hasError = true
            }

            if (!hasError) {
                try {
                    clienteRepository.saveCliente(state.toEntity())
                    _uiState.update {
                        it.copy(
                            successMessage = "Cliente guardado exitosamente",
                            nombresError = null,
                            telefonoError = null,
                            celularError = null,
                            rncError = null,
                            emailError = null,
                            direccionError = null
                        )
                    }
                    nuevo()
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessage = "Error al guardar el cliente")
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
                nombresError = null,
                telefonoError = null,
                celularError = null,
                rncError = null,
                emailError = null,
                direccionError = null,
                successMessage = null
            )
        }
    }

    fun onNombresChange(nombres: String) {
        _uiState.update {
            it.copy(nombres = nombres, nombresError = null, successMessage = null)
        }
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(telefono = telefono, telefonoError = null, successMessage = null)
        }
    }

    fun onCelularChange(celular: String) {
        _uiState.update {
            it.copy(celular = celular, celularError = null, successMessage = null)
        }
    }

    fun onRncChange(rnc: String) {
        _uiState.update {
            it.copy(rnc = rnc, rncError = null, successMessage = null)
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null, successMessage = null)
        }
    }
    class EmptyListException(message: String) : Exception(message)

    private fun getClientes() {
        viewModelScope.launch {
            try{
                val clientes = clienteRepository.getClientes()
                if (clientes.isEmpty()) {
                    throw EmptyListException("No se encontraron clientes")
                }
                _uiState.update { it.copy(clientes = clientes) }

            }catch (e: EmptyListException){
                _uiState.update {
                    it.copy(errorMessage = e.message, successMessage = null)
                }
            }
        }
    }

    fun onDireccionChange(direccion: String) {
        _uiState.update {
            it.copy(direccion = direccion, direccionError = null, successMessage = null)
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
        val nombresError: String? = null,
        val telefonoError: String? = null,
        val celularError: String? = null,
        val rncError: String? = null,
        val emailError: String? = null,
        val direccionError: String? = null,
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
