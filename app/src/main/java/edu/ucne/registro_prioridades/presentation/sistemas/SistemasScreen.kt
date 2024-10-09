@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.registro_prioridades.presentation.sistemas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SistemasScreen(viewModel: SistemasViewModel = hiltViewModel(), goBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SistemasBodyScreen(
        uiState = uiState,
        onNombreSistemaChange = viewModel::onNombreSistemaChange,
        save = viewModel::save,
        nuevo = viewModel::nuevo,
        goBack = goBack
    )
}

@Composable
fun SistemasBodyScreen(
    uiState: SistemasViewModel.Uistate,
    onNombreSistemaChange: (String) -> Unit,
    save: () -> Unit,
    nuevo: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Sistemas",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
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
                value = uiState.NombreSistema,
                onValueChange = onNombreSistemaChange
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
                    Icon(Icons.Filled.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { nuevo() }
                ) {
                    Text(text = "Nuevo")
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }



        }
    }
}
