package com.example.snappyshop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappyshop.screen.AuthScreen
import com.example.snappyshop.screen.HomeScreen
import com.example.snappyshop.screen.LoginScreen
import com.example.snappyshop.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val isLoggedIn = Firebase.auth.currentUser!=null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage) {
        composable("auth") {
            AuthScreen(modifier, navController)
        }

        composable("login") {
            LoginScreen(modifier, navController)
        }

        composable("signup") {
            SignupScreen(modifier, navController)
        }

        composable("home") {
            HomeScreen(modifier, navController)
        }
    }

}