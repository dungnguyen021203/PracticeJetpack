package com.example.snappyshop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappyshop.GlobalNavigation
import com.example.snappyshop.components.CartItemView
import com.example.snappyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CartPage(modifier: Modifier = Modifier) {

    val userModel = remember {
        mutableStateOf(UserModel())
    }
    val isCartEmpty = userModel.value.cartItems.isEmpty()

    DisposableEffect (Unit) {
        var listener = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { it, _ ->
                if (it != null) {
                    val result = it.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                    }
                }
            }
        onDispose { listener.remove() }
    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Your Cart", style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        if (isCartEmpty) {
            Text(
                text = "Your Cart is Empty",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(userModel.value.cartItems.toList(), key = { it.first }) { (productId, quantity) ->
                    CartItemView(modifier, productId, quantity)
                }
            }
        }

        Button(
            onClick = { GlobalNavigation.navController.navigate("checkout") },
            enabled = !isCartEmpty,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 16.dp)
        ) {
            Text(text = "Check out", fontSize = 16.sp)
        }
    }
}