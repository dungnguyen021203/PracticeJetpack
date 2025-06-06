package com.example.snappyshop

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappyshop.pages.CategoryProductPage
import com.example.snappyshop.pages.ProductDetailsPage
import com.example.snappyshop.screen.AuthScreen
import com.example.snappyshop.screen.HomeScreen
import com.example.snappyshop.screen.LoginScreen
import com.example.snappyshop.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

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

        composable("category-products/{categoryId}") {
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductPage(modifier, categoryId?:"")
        }

        composable("product-details/{productId}") {
            var productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId?:"")
        }
    }
}

object GlobalNavigation {
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}