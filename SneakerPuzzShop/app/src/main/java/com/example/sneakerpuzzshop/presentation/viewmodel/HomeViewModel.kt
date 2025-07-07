package com.example.sneakerpuzzshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerpuzzshop.domain.model.CategoryModel
import com.example.sneakerpuzzshop.domain.usecase.GetBannerUseCase
import com.example.sneakerpuzzshop.domain.usecase.GetCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBannerUseCase: GetBannerUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
): ViewModel() {
    private val _banner = MutableStateFlow<List<String>>(emptyList())
    val banner: StateFlow<List<String>> = _banner

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    init {
        loadBanners()
        loadCategories()
    }

    private fun loadBanners() {
        viewModelScope.launch {
            _banner.value = getBannerUseCase()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = getCategoryUseCase()
        }
    }
}