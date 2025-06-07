package com.example.snappyshop.pages

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.snappyshop.GlobalNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Button(onClick = {
        Firebase.auth.signOut()
        GlobalNavigation.navController.navigate("auth") {
            popUpTo("home") { inclusive = true }
        }
    }) {
        Text(text = "Sign Out")
    }
}