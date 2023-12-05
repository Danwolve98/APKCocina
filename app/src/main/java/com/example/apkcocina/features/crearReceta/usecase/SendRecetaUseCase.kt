package com.example.apkcocina.features.crearReceta.usecase

import com.example.apkcocina.features.crearReceta.repository.CrearRecetasRepository
import com.example.apkcocina.utils.model.Receta
import javax.inject.Inject

class SendRecetaUseCase @Inject constructor(private val crearRecetasRepository: CrearRecetasRepository) {
   suspend operator fun invoke(receta : Receta) = crearRecetasRepository.sendReceta(receta)
}