package com.example.snappyshop.model

data class ProductModel (
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val actualPrice: String = "",
    val category: String = "",
    val images: List<String> = emptyList()
)