package edu.ucne.registro_prioridades.presentation.navigation.prioridad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registro_prioridades.local.database.PrioridadDb
import edu.ucne.registro_prioridades.local.entities.PrioridadEntity
import kotlinx.coroutines.launch

@Composable
fun EditPrioridadScreen(
    prioridadDb: PrioridadDb,
    prioridadId: Int,
    goBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var prioridad by remember { mutableStateOf<PrioridadEntity?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var diasCompromiso by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    // Cargar la prioridad a editar
    LaunchedEffect(prioridadId) {
        val loadedPrioridad = prioridadDb.prioridadDao().find(prioridadId)
        prioridad = loadedPrioridad
        if (loadedPrioridad != null) {
            descripcion = loadedPrioridad.descripcion
            diasCompromiso = loadedPrioridad.diasCompromiso?.toString() ?: ""
        }
    }

    Scaffold(
        topBar = {
            Text(
                text = "Editar Prioridad",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripción") },
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    mensajeError = ""
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Días de Compromiso") },
                value = diasCompromiso,
                onValueChange = {
                    diasCompromiso = it
                    mensajeError = ""
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (mensajeExito.isNotEmpty()) {
                Text(
                    text = mensajeExito,
                    color = Color.Green,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            if (descripcion.isBlank() || diasCompromiso.isBlank()) {
                                mensajeError = "Debe de Completar Todos los Campos."
                                return@launch
                            }

                            val diasCompromisoValor = diasCompromiso.toIntOrNull()
                            if (diasCompromisoValor == null || diasCompromisoValor <= 0) {
                                mensajeError = "Días de Compromiso debe ser un número mayor que 0."
                                return@launch
                            }

                            val updatedPrioridad = PrioridadEntity(
                                prioridadId = prioridadId,
                                descripcion = descripcion,
                                diasCompromiso = diasCompromisoValor
                            )
                            prioridadDb.prioridadDao().save(updatedPrioridad)

                            mensajeExito = "Prioridad Actualizada con Éxito."

                            launch {
                                kotlinx.coroutines.delay(2000)
                                goBack()
                            }
                        }
                    }
                ) {
                    Text(text = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        goBack()
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}
