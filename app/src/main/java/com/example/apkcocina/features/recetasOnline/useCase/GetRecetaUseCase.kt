package com.example.apkcocina.features.recetasOnline.useCase

import com.example.apkcocina.features.recetasOnline.repository.RecetasRepository
import javax.inject.Inject

class GetRecetaUseCase @Inject constructor(private val recetasRepository: RecetasRepository) {
    suspend operator fun invoke(recetaId : String,collection : String) = recetasRepository.getReceta(recetaId,collection)
}