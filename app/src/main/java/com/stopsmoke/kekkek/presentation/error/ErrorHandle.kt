package com.stopsmoke.kekkek.presentation.error

import androidx.navigation.NavController

interface ErrorHandle {
    fun errorExit(navController: NavController) {
        navController.navigate("error") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}