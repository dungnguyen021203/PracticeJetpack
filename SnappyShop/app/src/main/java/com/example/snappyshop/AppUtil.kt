package com.example.snappyshop

import android.app.Activity
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import com.example.snappyshop.zalopay.Api.CreateOrder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

    fun getDiscountPercentage() : Float {
        return 10.0f
    }

    fun getTaxPercentage() : Float {
        return 13.0f
    }

    sealed class PaymentResult {
        object Success : PaymentResult()
        object Canceled : PaymentResult()
        data class Error(val error: ZaloPayError?) : PaymentResult()
    }

    fun startPayment(
        activity: Activity,
        amount: Float,
        callback: (PaymentResult) -> Unit
    ) {

        // Cho phép gọi API sync trong thread hiện tại (demo)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())

        // Khởi tạo SDK (gọi một lần cho cả app, đặt ở Application càng tốt)
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        // Tạo đơn hàng (API riêng của bạn)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val orderApi = CreateOrder()
                val data = orderApi.createOrder("%.0f".format(amount))
                if (data.getString("return_code") == "1") {
                    val token = data.getString("zp_trans_token")

                    // Chuyển về Main thread để gọi SDK
                    withContext(Dispatchers.Main) {
                        ZaloPaySDK.getInstance().payOrder(
                            activity,
                            token,
                            "demozpdk://app",
                            object : PayOrderListener {
                                override fun onPaymentSucceeded(
                                    transactionId: String?,
                                    transToken: String?,
                                    appTransId: String?
                                ) {
                                    callback(PaymentResult.Success)
                                }

                                override fun onPaymentCanceled(
                                    transToken: String?,
                                    appTransId: String?
                                ) {
                                    callback(PaymentResult.Canceled)
                                }

                                override fun onPaymentError(
                                    zaloPayError: ZaloPayError?,
                                    transToken: String?,
                                    appTransId: String?
                                ) {
                                    callback(PaymentResult.Error(zaloPayError))
                                }
                            }
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback(PaymentResult.Error(null))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(PaymentResult.Error(null))
                }
            }
        }
    }

}
