@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.registro_sistemas.presentation.sistemas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registro_prioridades.presentation.sistemas.SistemasViewModel

@Composable
fun SistemasEditScreen(
    viewModel: SistemasViewModel = hiltViewModel(),
    sistemaId: Int,
    goBack: () -> Unit
) {
    LaunchedEffect(sistemaId) {
        viewModel.select(sistemaId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SistemasEditBodyScreen(
        uiState = uiState,
        onNombreChange = viewModel::onNombreSistemaChange,
        save = viewModel::save,
        goBack = goBack
    )
}

@Composable
fun SistemasEditBodyScreen(
    uiState: SistemasViewModel.Uistate,
    onNombreChange: (String) -> Unit,
    save: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Editar Sistema",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = save) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
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
                label = { Text(text = "Nombre del Sistema") },
                value = uiState.NombreSistema ?: "",
                onValueChange = onNombreChange
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { save() }
                ) {
                    Text(text = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { goBack() }
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}
