package com.example.snappyshop.model

import com.google.firebase.Timestamp

data class Order(
    val id: String = "",
    val userId: String = "",
    val date: Timestamp = Timestamp.now(),
    val items: Map<String, Long> = mapOf(),
    val status: String = "",
    val address: String
)
