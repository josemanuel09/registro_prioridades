package edu.ucne.registro_prioridades.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registro_prioridades.ScreenPrioridades
import edu.ucne.registro_prioridades.local.database.PrioridadDb
import edu.ucne.registro_prioridades.presentation.navigation.prioridad.PrioridadListScreen
import edu.ucne.registro_prioridades.presentation.navigation.prioridad.EditPrioridadScreen
import edu.ucne.registro_prioridades.presentation.navigation.prioridad.DeletePrioridadScreen
import edu.ucne.registro_prioridades.presentation.navigation.prioridad.PrioridadScreen

import kotlinx.coroutines.launch

@Composable
fun registro_prioridadesNavHost(prioridadDb: PrioridadDb, navHostController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val prioridadList by prioridadDb.prioridadDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navHostController,
        startDestination = ScreenPrioridades.PrioridadesList
    ) {
        composable<ScreenPrioridades.PrioridadesList> {
            PrioridadListScreen(
                prioridadList,
                createPrioridad = {
                    navHostController.navigate(ScreenPrioridades.Prioridades(0))
                },
                editPrioridad = { prioridad ->
                    navHostController.navigate(ScreenPrioridades.EditPrioridad(prioridad.prioridadId ?: 0))
                },
                deletePrioridad = { prioridad ->
                    navHostController.navigate(ScreenPrioridades.DeletePrioridad(prioridad.prioridadId ?: 0))
                }
            )
        }

        composable<ScreenPrioridades.Prioridades> {
            val args = it.toRoute<ScreenPrioridades.Prioridades>()
            PrioridadScreen(
                prioridadDb = prioridadDb,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<ScreenPrioridades.EditPrioridad> {
            val args = it.toRoute<ScreenPrioridades.EditPrioridad>()
            EditPrioridadScreen(
                prioridadDb = prioridadDb,
                prioridadId = args.prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<ScreenPrioridades.DeletePrioridad> {
            val args = it.toRoute<ScreenPrioridades.DeletePrioridad>()
            DeletePrioridadScreen(
                prioridadDb = prioridadDb,
                prioridadId = args.prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}
