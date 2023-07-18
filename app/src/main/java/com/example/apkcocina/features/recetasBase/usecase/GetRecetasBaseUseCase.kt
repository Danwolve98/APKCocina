package com.example.apkcocina.features.recetasBase.usecase

import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.recetasBase.repository.RecetasBaseRespoitory
import javax.inject.Inject

class GetRecetasBaseUseCase @Inject constructor(val recetasBaseRespoitory: RecetasBaseRespoitory) {

    suspend operator fun invoke() : List<Receta> = recetasBaseRespoitory.getRecetas()
}