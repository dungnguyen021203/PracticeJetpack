package com.example.sneakerpuzzshop.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sneakerpuzzshop.presentation.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel?, navController: NavController) {
    Text(text = "Something")
    TextButton(
        onClick = {
            viewModel?.signOut()
            navController.popBackStack()
            navController.navigate("login")
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Sign out", fontSize = 18.sp)
    }
}