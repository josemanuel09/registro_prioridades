package edu.ucne.registro_prioridades.presentation.ticket

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registro_prioridades.presentation.tickets.TicketViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketScreen(viewModel: TicketViewModel = hiltViewModel(), goBack: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        uiState = uiState.value,
        onFechaChange = viewModel::onFechaChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onClienteChange = viewModel::onClienteChange,
        onSistemaChange = viewModel::onSistemaChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onSolicitadoPorChange = viewModel::onSolicitadoPorChange,
        save = viewModel::saveTicket,
        nuevo = viewModel::nuevo,
        goBack = goBack
    )
}

@Composable
fun TicketBodyScreen(
    uiState: TicketViewModel.UiState,
    onFechaChange: (Date) -> Unit,
    onPrioridadChange: (Int) -> Unit,
    onClienteChange: (Int) -> Unit,
    onSistemaChange: (Int) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSolicitadoPorChange: (String) -> Unit,
    save: () -> Unit,
    nuevo: () -> Unit,
    goBack: () -> Unit
) {
    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = uiState.fecha.let{ date.format(it)} ?: ""

    var showDatePicker by remember { mutableStateOf(false) }

    var clienteExpanded by remember { mutableStateOf(false) }
    var prioridadExpanded by remember { mutableStateOf(false) }
    var sistemaExpanded by remember { mutableStateOf(false) }

    var selectedPrioridadText by remember { mutableStateOf("Seleccionar Prioridad") }
    var selectedClientesText by remember { mutableStateOf("Seleccionar Cliente") }
    var selectedSistemaText by remember { mutableStateOf("Seleccionar Sistema") }

    val prioridades = uiState.prioridades
    val clientes = uiState.clientes
    val sistemas = uiState.sistemas

    if (uiState.prioridadId == null) selectedPrioridadText = "Seleccionar Prioridad"
    if (uiState.clienteId == null) selectedClientesText = "Seleccionar Cliente"
    if (uiState.sistemaId == null) selectedSistemaText = "Seleccionar Sistema"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Registro de Tickets",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = TextStyle(
                    fontSize = 20.sp,
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
                .padding(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Fecha") },
                value = formattedDate,
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Menú desplegable",
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                }

            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { clienteExpanded = true },
                value = selectedClientesText,
                onValueChange = { },
                readOnly = true,
                label = { Text("Cliente") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Menú desplegable",
                        modifier = Modifier.clickable { clienteExpanded = !clienteExpanded }
                    )
                }
            )
            DropdownMenu(
                expanded = clienteExpanded,
                onDismissRequest = { clienteExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                clientes.forEach { cliente ->
                    DropdownMenuItem(
                        text = { Text(cliente.nombres) },
                        onClick = {
                            onClienteChange(cliente.clienteId ?: 0)
                            selectedClientesText = cliente.nombres
                            clienteExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { prioridadExpanded = true },
                value = selectedPrioridadText,
                onValueChange = { },
                readOnly = true,
                label = { Text("Prioridad") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Menú desplegable",
                        modifier = Modifier.clickable { prioridadExpanded = !prioridadExpanded }
                    )
                }
            )
            DropdownMenu(
                expanded = prioridadExpanded,
                onDismissRequest = { prioridadExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                prioridades.forEach { prioridad ->
                    DropdownMenuItem(
                        text = { Text(prioridad.descripcion) },
                        onClick = {
                            onPrioridadChange(prioridad.prioridadId ?: 0)
                            selectedPrioridadText = prioridad.descripcion
                            prioridadExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { sistemaExpanded = true },
                value = selectedSistemaText,
                onValueChange = { },
                readOnly = true,
                label = { Text("Sistema") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Menú desplegable",
                        modifier = Modifier.clickable { sistemaExpanded = !sistemaExpanded }
                    )
                }
            )
            DropdownMenu(
                expanded = sistemaExpanded,
                onDismissRequest = { sistemaExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                sistemas.forEach { sistema ->
                    DropdownMenuItem(
                        text = { Text(sistema.nombreSistema) },
                        onClick = {
                            onSistemaChange(sistema.sistemaId ?: 0)
                            selectedSistemaText = sistema.nombreSistema
                            sistemaExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Solicitado Por") },
                value = uiState.solicitadoPor,
                onValueChange = onSolicitadoPorChange
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { save() }
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { nuevo()
                        selectedPrioridadText = "Seleccionar Prioridad"
                        selectedClientesText = "Seleccionar Cliente"
                        selectedSistemaText = "Seleccionar Sistema"

                    }
                ) {
                    Text(text = "Nuevo")
                    Icon(Icons.Default.Refresh, contentDescription = "Nuevo")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}



