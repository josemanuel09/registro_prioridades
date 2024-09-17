@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.registro_prioridades.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person // Puedes cambiar el icono si lo deseas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registro_prioridades.NavigationItem
import edu.ucne.registro_prioridades.presentation.prioridad.*
import edu.ucne.registro_prioridades.presentation.ticket.*
import kotlinx.coroutines.CoroutineScope
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
                    onEditPrioridad = { prioridad ->
                        navHostController.navigate(ScreenPrioridades.EditPrioridad(prioridad))
                    },
                    onDeletePrioridad = { prioridad ->
                        navHostController.navigate(ScreenPrioridades.DeletePrioridad(prioridad))
                    }
                )
            }
            composable<ScreenPrioridades.TicketsList> {
                TicketListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createTicket = {
                        navHostController.navigate(ScreenPrioridades.Tickets(0))
                    },
                    onEditTicket = { ticket ->
                        navHostController.navigate(ScreenPrioridades.EditTicket(ticket))
                    },
                    onDeleteTicket = { ticket ->
                        navHostController.navigate(ScreenPrioridades.DeleteTicket(ticket))
                    }
                )
            }
            composable<ScreenPrioridades.Prioridades> {
                val args = it.toRoute<ScreenPrioridades.Prioridades>()
                PrioridadScreen(
                    goBack = {
                        navHostController.navigateUp()
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
            composable<ScreenPrioridades.EditPrioridad> {
                val args = it.toRoute<ScreenPrioridades.EditPrioridad>()
                EditPrioridadScreen(
                    prioridadId = args.prioridadId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<ScreenPrioridades.EditTicket> {
                val args = it.toRoute<ScreenPrioridades.EditTicket>()
                EditTicketScreen(
                    ticketId = args.ticketId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<ScreenPrioridades.DeletePrioridad> {
                val args = it.toRoute<ScreenPrioridades.DeletePrioridad>()
                DeletePrioridadScreen(
                    prioridadId = args.prioridadId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<ScreenPrioridades.DeleteTicket> {
                val args = it.toRoute<ScreenPrioridades.DeleteTicket>()
                DeleteTicketScreen(
                    ticketId = args.ticketId,
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
