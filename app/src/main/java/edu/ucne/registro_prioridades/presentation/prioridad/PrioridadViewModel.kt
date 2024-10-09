package edu.ucne.registro_prioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registro_prioridades.data.local.entities.PrioridadEntity
import edu.ucne.registro_prioridades.data.remote.dto.PrioridadDto
import edu.ucne.registro_prioridades.data.repository.PrioridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            when {
                state.descripcion.isBlank() -> {
                    _uiState.update {
                        it.copy(errorMessages = "La descripción no puede estar vacía", successMessage = null)
                    }
                }
                state.diasCompromiso <= 0 -> {
                    _uiState.update {
                        it.copy(errorMessages = "Los días de compromiso deben ser mayores que cero", successMessage = null)
                    }
                }
                else -> {
                    try {
                        prioridadRepository.save(state.toEntity())
                        _uiState.update {
                            it.copy(
                                successMessage = "Prioridad guardada exitosamente",
                                errorMessages = null
                            )
                        }
                        nuevo()
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(errorMessages = "Error al guardar la prioridad", successMessage = null)
                        }
                    }
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                diasCompromiso = 0,
                errorMessages = null,
                successMessage = null
            )
        }
    }

    fun selectedPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.getPrioridad(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: "",
                        diasCompromiso = prioridad?.diasCompromiso ?: 0,
                        errorMessages = null,
                        successMessage = null
                    )
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            try {
                prioridadRepository.delete(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(
                        successMessage = "Prioridad eliminada exitosamente",
                        errorMessages = null
                    )
                }
                nuevo()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessages = "Error al eliminar la prioridad", successMessage = null)
                }
            }
        }
    }

    fun getPrioridades() {
        viewModelScope.launch {
            try {
                val prioridades = prioridadRepository.getAll()
                if (prioridades.isEmpty()) {
                    throw EmptyListException("La lista de prioridades está vacía")
                }
                _uiState.update { it.copy(prioridades = prioridades) }
            } catch (e: EmptyListException) {
                _uiState.update { it.copy(errorMessages = e.message, successMessage = null) }
            }
        }
    }
    class EmptyListException(message: String) : Exception(message)

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion, errorMessages = null, successMessage = null)
        }
    }

    fun onDiasCompromisoChange(diasCompromiso: Int) {
        _uiState.update {
            it.copy(diasCompromiso = diasCompromiso, errorMessages = null, successMessage = null)
        }
    }

    data class UiState(
        val prioridadId: Int? = null,
        val descripcion: String = "",
        val diasCompromiso: Int = 0,
        val errorMessages: String? = null,
        val successMessage: String? = null,
        val prioridades: List<PrioridadDto> = emptyList()
    )

    fun UiState.toEntity() = PrioridadDto(
        prioridadId = prioridadId,
        descripcion = descripcion,
        diasCompromiso = diasCompromiso
    )
}
