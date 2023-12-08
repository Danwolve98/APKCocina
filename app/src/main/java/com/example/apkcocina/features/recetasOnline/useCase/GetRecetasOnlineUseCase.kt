package com.example.apkcocina.features.recetasOnline.useCase

import com.example.apkcocina.features.recetasOnline.repository.RecetasRepository
import javax.inject.Inject

class GetRecetasOnlineUseCase @Inject constructor(private val recetasRepository: RecetasRepository) {
    suspend operator fun invoke() = recetasRepository.getRecetas()
}