package com.example.sneakerpuzzshop.domain.usecase

import com.example.sneakerpuzzshop.domain.repository.HomeRepository
import javax.inject.Inject

class GetBannerUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): List<String> {
        return homeRepository.getBanners()
    }
}