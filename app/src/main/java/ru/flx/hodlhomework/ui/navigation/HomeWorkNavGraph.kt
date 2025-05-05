package ru.flx.hodlhomework.ui.navigation

import android.widget.Toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import ru.flx.hodlhomework.repositories.navigation.ScreenList
import ru.flx.hodlhomework.repositories.notification.NotificationEvent
import ru.flx.hodlhomework.ui.home.HomeRoute

@Composable
fun HomeWorkNavGraph(
    navController: NavHostController
) {

    val state = hiltViewModel<HomeWorkNavGraphViewModel>()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        state.notificationSystem.events.collect { event ->
            when (event) {
                is NotificationEvent.ShowToast -> {
                    Toast.makeText(context, event.text, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = ScreenList.Home.name
    ) {
        composable(
            route = ScreenList.Home.name
        ) {
            HomeRoute()
        }
        composable(
            route = ScreenList.Transaction.name
        ) {

        }
    }
}