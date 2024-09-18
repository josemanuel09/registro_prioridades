package edu.ucne.registro_prioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registro_prioridades.presentation.navigation.registro_prioridadesNavHost

@AndroidEntryPoint

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Registro_PrioridadesTheme {
                val navHost = rememberNavController()
                val items = NavigationItems()
                registro_prioridadesNavHost(navHostController = navHost, items = items)

            }
        }
    }


}

fun NavigationItems() : List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = "Prioridades",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,

        ),
        NavigationItem(
            title = "Tickets",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Build,
        )
    )
}

@Composable
fun Registro_PrioridadesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}
