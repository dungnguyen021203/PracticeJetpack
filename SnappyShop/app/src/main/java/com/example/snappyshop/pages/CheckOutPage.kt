package com.example.snappyshop.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappyshop.AppUtil
import com.example.snappyshop.GlobalNavigation
import com.example.snappyshop.model.ProductModel
import com.example.snappyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@SuppressLint("ContextCastToActivity")
@Composable
fun CheckOutPage(modifier: Modifier = Modifier) {

    val userModel = remember {
        mutableStateOf(UserModel())
    }

    val productList = remember {
        mutableStateListOf(ProductModel())
    }

    val subTotal = remember {
        mutableStateOf(0f)
    }

    val discount = remember {
        mutableStateOf(0f)
    }

    val tax = remember {
        mutableStateOf(0f)
    }

    val total = remember {
        mutableStateOf(0f)
    }

    val activity = LocalContext.current as Activity

//    fun calculateAndAssign() {
//        productList.forEach {
//            if (it.actualPrice.isNotEmpty()) {
//                val qty = userModel.value.cartItems[it.id] ?: 0
//                subTotal.value += it.actualPrice.replace(",", ".").toFloat() * qty // Loi NumberFormat
//            }
//        }
//
//        discount.value = subTotal.value * (AppUtil.getDiscountPercentage()) / 100
//        tax.value = subTotal.value * (AppUtil.getTaxPercentage()) / 100
//        total.value = "%.2f".format(subTotal.value - discount.value + tax.value).toFloat()
//    }

    fun calculateAndAssign() {
        subTotal.value = 0f // Reset subtotal to avoid accumulating on recomposition
        productList.forEach {
            if (it.actualPrice.isNotEmpty()) {
                val qty = userModel.value.cartItems[it.id] ?: 0
                try {
                    subTotal.value += it.actualPrice.replace(",", ".").toFloat() * qty
                } catch (e: NumberFormatException) {
                    Log.e("TAG", e.toString())
                }
            }
        }

        discount.value = subTotal.value * (AppUtil.getDiscountPercentage()) / 100
        tax.value = subTotal.value * (AppUtil.getTaxPercentage()) / 100
        total.value = "%.2f".format(subTotal.value - discount.value + tax.value).replace(",", ".").toFloat()
    }


    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result

                        Firebase.firestore.collection("data")
                            .document("stock").collection("products")
                            .whereIn("id", userModel.value.cartItems.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val resultProduct =
                                        task.result.toObjects(ProductModel::class.java)
                                    productList.addAll(resultProduct)
                                    calculateAndAssign()
                                }
                            }
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Check Out", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Deliver to : ", fontWeight = FontWeight.SemiBold)
        Text(text = userModel.value.name)
        Text(text = userModel.value.address)
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        RowCheckOutItem("Subtotal", subTotal.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckOutItem("Tax (+)", tax.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckOutItem("Discount (-)", discount.value.toString())
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()

        Text(text = "To Pay", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text(
            text = "$" + total.value.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                AppUtil.startPayment(activity, total.value) { result ->
                    when (result) {
                        is AppUtil.PaymentResult.Success  -> AppUtil.showToast(activity, "Thanh toán thành công")
                        is AppUtil.PaymentResult.Canceled -> AppUtil.showToast(activity, "Đã hủy thanh toán")
                        is AppUtil.PaymentResult.Error    -> AppUtil.showToast(activity, "Có lỗi xảy ra")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Pay Now", fontSize = 16.sp)
        }

    }
}

@Composable
fun RowCheckOutItem(title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "$$value", fontSize = 18.sp)
    }

}