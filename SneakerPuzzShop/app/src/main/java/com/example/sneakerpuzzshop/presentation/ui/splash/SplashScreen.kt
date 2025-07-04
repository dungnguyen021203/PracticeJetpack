package com.example.sneakerpuzzshop.presentation.ui.splash


import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sneakerpuzzshop.presentation.components.AuthHeader
import com.example.sneakerpuzzshop.presentation.viewmodel.AuthViewModel
import com.example.sneakerpuzzshop.utils.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: AuthViewModel?, navController: NavController) {
    LaunchedEffect(Unit) {
        delay(4000)
        val user = viewModel?.currentUser
        if (user != null) {
            navController.navigate(ROUTE_HOME) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(ROUTE_LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthHeader()
    }
}
