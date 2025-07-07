package com.example.sneakerpuzzshop.data.repository

import com.example.sneakerpuzzshop.domain.model.CategoryModel
import com.example.sneakerpuzzshop.domain.repository.HomeRepository
import com.example.sneakerpuzzshop.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
): HomeRepository {
    override suspend fun getBanners(): List<String> {
        return try {
            val snapshot = firestore.collection("data")
                .document("banners").get().await()
            val result = snapshot.get("urls") as? List<*>
            result?.filterIsInstance<String>() ?: emptyList()
            // tránh crash khi ép sai kiểu, lọc ra đúng phần tử kiểu String trong list.
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    }

    override suspend fun getCategories(): List<CategoryModel> {
        val result = firestore.collection("data")
            .document("stocks")
            .collection("categories").get().await()
        return result.documents.mapNotNull { doc ->
            doc.toObject(CategoryModel::class.java)
        }
    }
}