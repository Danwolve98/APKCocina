package com.example.apkcocina.features.recetasBase.usecase

import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.recetasBase.repository.RecetasDataBaseRespoitory
import javax.inject.Inject

class GetRecetasBaseUseCase @Inject constructor(private val recetasDataBaseRespoitory: RecetasDataBaseRespoitory) {
    suspend operator fun invoke() = recetasDataBaseRespoitory.getRecetas()
}