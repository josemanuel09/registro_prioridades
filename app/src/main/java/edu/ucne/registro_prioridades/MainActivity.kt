package edu.ucne.registro_prioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registro_prioridades.local.database.PrioridadDb
import edu.ucne.registro_prioridades.local.entities.PrioridadEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prioridadDb = Room.databaseBuilder(
            applicationContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            Registro_PrioridadesTheme {
                PrioridadScreen(prioridadDb)
            }
        }
    }

    @Composable
    fun PrioridadScreen(prioridadDb: PrioridadDb) {
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
                    label = { Text(text = "Días compromiso") },
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

                val prioridadList by prioridadDb.prioridadDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycle = lifecycle,
                        minActiveState = Lifecycle.State.STARTED
                    )

                PrioridadListScreen(prioridadList)
            }
        }
    }

    @Composable
    fun PrioridadListScreen(prioridadList: List<PrioridadEntity>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Text(
                text = "Lista Prioridades",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 8.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Descripción",
                    modifier = Modifier.weight(3f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Días Compromiso",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(prioridadList) { prioridad ->
                    PrioridadRow(prioridad)
                }
            }
        }
    }

    @Composable
    fun PrioridadRow(prioridad: PrioridadEntity) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = prioridad.prioridadId.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = prioridad.descripcion,
                modifier = Modifier.weight(3f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = prioridad.diasCompromiso.toString(),
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 16.sp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
    }
}

@Composable
fun Registro_PrioridadesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}
