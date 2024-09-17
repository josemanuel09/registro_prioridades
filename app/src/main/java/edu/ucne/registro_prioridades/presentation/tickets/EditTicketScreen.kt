package edu.ucne.registro_prioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditTicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int,
    goBack: () -> Unit
) {
    LaunchedEffect(ticketId) {
        viewModel.selectTicket(ticketId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EditTicketBodyScreen(
        uiState = uiState,
        onFechaChange = viewModel::onFechaChange,
        onPrioridadChange = viewModel::onPrioridadIdChange,
        onClienteChange = viewModel::onClienteChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        save = viewModel::saveTicket,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTicketBodyScreen(
    uiState: TicketViewModel.UiState,
    onFechaChange: (String) -> Unit,
    onPrioridadChange: (Int?) -> Unit,
    onClienteChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    save: () -> Unit,
    goBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPrioridadText by remember {
        mutableStateOf(
            uiState.prioridades.find { it.prioridadId == uiState.prioridadId }?.descripcion
                ?: "Seleccionar Prioridad"
        )
    }

    val prioridades = uiState.prioridades

    LaunchedEffect(uiState.prioridadId) {
        val selectedPrioridad = prioridades.find { it.prioridadId == uiState.prioridadId }
        selectedPrioridadText = selectedPrioridad?.descripcion ?: "Seleccionar Prioridad"
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = "Editar Ticket",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedPrioridadText,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = "Menú desplegable",
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    prioridades.forEach { prioridad ->
                        DropdownMenuItem(
                            text = { Text(prioridad.descripcion) },
                            onClick = {
                                onPrioridadChange(prioridad.prioridadId)
                                selectedPrioridadText = prioridad.descripcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Fecha") },
                value = uiState.fecha,
                onValueChange = onFechaChange
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange
            )
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
                    Icon(Icons.Filled.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { goBack() }
                ) {
                    Text(text = "Cancelar")
                    Icon(Icons.Filled.Refresh, contentDescription = "Cancelar")
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
