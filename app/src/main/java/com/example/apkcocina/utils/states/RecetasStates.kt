package com.example.apkcocina.utils.states

import com.example.apkcocina.utils.model.Receta

sealed class CrearRecetaState {
    data object Successfull : CrearRecetaState()
    data class Error(val error : String) : CrearRecetaState()
}

sealed class RecetasOnlineState {
    data class Successfull(val listRecetas: List<Receta>) : RecetasOnlineState()
    data class Error(val error : String) : RecetasOnlineState()
}