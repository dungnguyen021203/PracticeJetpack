package com.example.snappyshop.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snappyshop.components.ProductItemView
import com.example.snappyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductPage(modifier: Modifier = Modifier, categoryId: String) {
    var productList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    productList.value = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }

                    // productList.value = resultList.plus(resultList).plus(resultList)
                    // Add more items for testing purpose
                }
            }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 30.dp)) {
        items(productList.value.chunked(2)) {rowItems ->
            Row {
                rowItems.forEach {
                    ProductItemView(product = it, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}