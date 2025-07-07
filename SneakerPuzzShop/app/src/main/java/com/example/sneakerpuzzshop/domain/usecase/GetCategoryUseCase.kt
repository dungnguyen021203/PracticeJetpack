package com.example.sneakerpuzzshop.domain.usecase

import com.example.sneakerpuzzshop.domain.model.CategoryModel
import com.example.sneakerpuzzshop.domain.repository.HomeRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): List<CategoryModel> {
        return homeRepository.getCategories()
    }
}