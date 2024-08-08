package com.stopsmoke.kekkek.presentation.error

import androidx.navigation.NavController

fun errorExit(navController: NavController) {
    navController.navigate("errorServerEtc") {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun errorMissing(navController: NavController) {
    navController.navigate("errorMissing")
}