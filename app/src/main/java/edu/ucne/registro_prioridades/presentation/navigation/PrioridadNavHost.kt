@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.registro_prioridades.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.prioridadregistro.presentation.clientes.ClientesScreen
import edu.ucne.registro_prioridades.NavigationItem
import edu.ucne.registro_prioridades.presentation.clientes.ClientesListScreen

import edu.ucne.registro_prioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.registro_prioridades.presentation.prioridad.PrioridadScreen
import edu.ucne.registro_prioridades.presentation.sistemas.SistemaListScreen
import edu.ucne.registro_prioridades.presentation.sistemas.SistemasScreen
import edu.ucne.registro_prioridades.presentation.ticket.TicketListScreen
import edu.ucne.registro_prioridades.presentation.ticket.TicketScreen
import kotlinx.coroutines.launch

@Composable
fun registro_prioridadesNavHost(
    items: List<NavigationItem>,
    navHostController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(Color(0xFFF5F5F5))
            ) {
                DrawerHeader(
                    modifier = Modifier.padding(16.dp),
                    text = "Registro Prioridades"
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                items.forEachIndexed { index, navigationItem ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = navigationItem.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (index == selectedItem) Color.Blue else Color.Black
                            )
                        },
                        selected = index == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = index
                            when (navigationItem.title) {
                                "Prioridades" -> navHostController.navigate(ScreenPrioridades.PrioridadesList)
                                "Tickets" -> navHostController.navigate(ScreenPrioridades.TicketsList)
                                "Sistemas" -> navHostController.navigate(ScreenPrioridades.SistemasList)
                                "Clientes" -> navHostController.navigate(ScreenPrioridades.ClientesList)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        NavHost(
            navController = navHostController,
            startDestination = ScreenPrioridades.PrioridadesList
        ) {
            composable<ScreenPrioridades.PrioridadesList> {
                PrioridadListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createPrioridad = {
                        navHostController.navigate(ScreenPrioridades.Prioridades(0))
                    },

                )
            }
            composable<ScreenPrioridades.TicketsList> {
                TicketListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createTicket = {
                        navHostController.navigate(ScreenPrioridades.Tickets(0))
                    }
                )
            }
            composable<ScreenPrioridades.SistemasList>{
                SistemaListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createSistema = {
                        navHostController.navigate(ScreenPrioridades.Sistemas(0))

                    },
                    onEditSistema = { sistema ->
                        navHostController.navigate(ScreenPrioridades.EditSistema(sistema))
                    },
                    onDeleteSistema = { sistema ->
                        navHostController.navigate(ScreenPrioridades.DeleteSistema(sistema))
                    }

                )
            }
            composable<ScreenPrioridades.ClientesList>{
                ClientesListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createCliente = {
                        navHostController.navigate(ScreenPrioridades.Clientes(0))
                    }

                )

            }


            composable<ScreenPrioridades.Prioridades> {
                val args = it.toRoute<ScreenPrioridades.Prioridades>()
                PrioridadScreen(
                    goBack = {
                        navHostController.navigate(ScreenPrioridades.PrioridadesList)
                    }
                )
            }
            composable<ScreenPrioridades.Tickets> {
                val args = it.toRoute<ScreenPrioridades.Tickets>()
                TicketScreen(
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<ScreenPrioridades.Sistemas>{
                val  args = it.toRoute<ScreenPrioridades.Sistemas>()
                SistemasScreen(
                    goBack = {
                        navHostController.navigate(ScreenPrioridades.SistemasList)
                    }
                )
            }
            composable<ScreenPrioridades.Clientes>{
                val args = it.toRoute<ScreenPrioridades.Clientes>()
                ClientesScreen(
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }

        }
    }
}

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2196F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Icono de usuario",
            tint = Color.White,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF1976D2))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}
