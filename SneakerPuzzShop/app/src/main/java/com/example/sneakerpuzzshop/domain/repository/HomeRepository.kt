package com.example.sneakerpuzzshop.domain.repository

import com.example.sneakerpuzzshop.domain.model.CategoryModel


interface HomeRepository {
    suspend fun getBanners(): List<String>

    suspend fun getCategories(): List<CategoryModel>
}