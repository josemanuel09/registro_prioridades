package edu.ucne.registro_prioridades.presentation.navigation.prioridad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registro_prioridades.local.database.PrioridadDb
import edu.ucne.registro_prioridades.local.entities.PrioridadEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PrioridadScreen(prioridadDb: PrioridadDb, goBack: () -> Unit) {
    var descripcion by remember { mutableStateOf("") }
    var diasCompromiso by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Registro de Prioridades",
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
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
                label = { Text (text = "Días de Compromiso") },
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
                            if (diasCompromisoValor == null || diasCompromisoValor <= 0){
                                mensajeError = "Días de Compromiso debe ser un numero mayor que 0."
                                return@launch
                            }

                            val prioridadExiste = prioridadDb.prioridadDao().findByDescripcion(descripcion)
                            if (prioridadExiste != null) {
                                mensajeError = "Prioridad Existente con la misma descripción."
                                return@launch
                            }

                            val nuevaPrioridad = PrioridadEntity(
                                descripcion = descripcion,
                                diasCompromiso = diasCompromiso.toIntOrNull() ?: 0
                            )
                            prioridadDb.prioridadDao().save(nuevaPrioridad)
                            descripcion = ""
                            diasCompromiso = ""

                            mensajeExito = "Prioridad Guardada con Éxito."

                            launch {
                                delay(5000)
                                mensajeExito = ""
                            }
                        }
                    }
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        descripcion = ""
                        diasCompromiso = ""
                        mensajeError = ""
                    }
                ) {
                    Text(text = "Nuevo")
                    Icon(Icons.Default.Refresh, contentDescription = "Nuevo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



        }
    }
}