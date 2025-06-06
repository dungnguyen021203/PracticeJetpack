package com.example.snappyshop.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snappyshop.AppUtil
import com.example.snappyshop.GlobalNavigation
import com.example.snappyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CartItemView(modifier: Modifier = Modifier, productId: String, quantity: Long) {
    var product by remember {
        mutableStateOf(ProductModel())
    }

    var context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").document(productId)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null)
                        product = result
                }
            }
    }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
            )

            Column(modifier = Modifier.padding(8.dp).weight(1f)) {
                Text(
                    text = product.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "$" + product.actualPrice,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { AppUtil.removeItemToCart(productId, context)}) {
                        Text(text = "-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "$quantity", fontSize = 16.sp)
                    IconButton(onClick = { AppUtil.addItemToCart(productId, context)}) {
                        Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }


            IconButton(onClick = { AppUtil.removeItemToCart(productId, context, true) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from cart"
                )
            }
        }
    }
}