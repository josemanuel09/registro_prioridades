package edu.ucne.registro_prioridades.presentation.sistemas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registro_prioridades.data.remote.dto.SistemasDto
import edu.ucne.registro_prioridades.data.repository.SistemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SistemasViewModel @Inject constructor(
    private val sistemasRepository: SistemaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(Uistate())
    val uiState = _uiState.asStateFlow()

    init {
        getSistemas()
    }

    fun save() {
        viewModelScope.launch {
            if (_uiState.value.NombreSistema.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "El nombre del sistema no puede estar vacío")
                }
                return@launch // Salir de la función si está vacío
            }
            try {
                sistemasRepository.save(_uiState.value.toEntity())
                nuevo()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al guardar el sistema: ${e.message}")
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                sistemaId = 0,
                NombreSistema = "",
                errorMessage = null
            )
        }
    }

    fun select(sistemaId: Int) {
        viewModelScope.launch {
            if (sistemaId > 0) {
                val sistemas = sistemasRepository.getSistema(sistemaId)
                _uiState.update {
                    it.copy(
                        sistemaId = sistemas?.sistemaId,
                        NombreSistema = sistemas?.nombreSistema ?: ""
                    )
                }
            }
        }
    }

    fun onNombreSistemaChange(nombre: String) {
        _uiState.update {
            it.copy(NombreSistema = nombre)
        }
    }

    fun delete() {
        viewModelScope.launch {
            sistemasRepository.delete(_uiState.value.sistemaId ?: return@launch)
            nuevo()
        }
    }

    fun getSistemas() {
        viewModelScope.launch {
            val sistemas = sistemasRepository.getAll()
            _uiState.update { it.copy(sistemas = sistemas) }
        }
    }

    data class Uistate(
        val sistemaId: Int? = null,
        val NombreSistema: String = "",
        val errorMessage: String? = null,
        val sistemas: List<SistemasDto> = emptyList()
    )

    fun Uistate.toEntity() = SistemasDto (
        sistemaId = sistemaId,
        nombreSistema = NombreSistema
    )


}
