package com.example.snappyshop

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

object AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun addItemToCart(productId: String, context: Context) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity + 1

                val updatedCard = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCard)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            showToast(context, "Item added successfully")
                        } else {
                            showToast(context, "Failed add")
                        }
                    }
            }
        }
    }

    fun removeItemToCart(productId: String, context: Context, removeAll: Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity - 1

                val updatedCard =

                    if (updatedQuantity <= 0 || removeAll == true) {
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    } else
                        mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCard)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            showToast(context, "Item removed successfully")
                        } else {
                            showToast(context, "Failed remove")
                        }
                    }
            }
        }
    }
}