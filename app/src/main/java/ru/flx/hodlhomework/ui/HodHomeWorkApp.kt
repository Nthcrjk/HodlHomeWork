package ru.flx.hodlhomework.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.HodlHomeWorkTheme
import ru.flx.hodlhomework.ui.navigation.HomeWorkNavGraph

@Composable
fun HodHomeWorkApp() {
    HodlHomeWorkTheme {
        var navGraph = rememberNavController()

        HomeWorkNavGraph(
            navController = navGraph
        )
    }
}