package com.example.apkcocina.utils.states

import com.example.apkcocina.utils.model.Receta

class CrearRecetaStateUI(var isLoading: Boolean = false)

sealed class SendRecetaState {
    data object Successfull : SendRecetaState()
    data class Error(val error : String) : SendRecetaState()
}

sealed class RecetasOnlineState {
    data class Successfull(val listRecetas: List<Receta>) : RecetasOnlineState()
    data class Error(val error : String) : RecetasOnlineState()
}

sealed class RecetaState {
    data class Successfull(val receta: Receta,val isFav : Boolean = false) : RecetaState()
    data class Error(val error : String) : RecetaState()
}

sealed class RandomRecetaState {
    data class Successfull(val idRecetaRandom: String, val nombreReceta : String) : RandomRecetaState()
    data class Error(val error : String) : RandomRecetaState()
}

sealed class SetRecetaFavState {
    data object Successfull : SetRecetaFavState()
    data class Error(val error : String) : SetRecetaFavState()
}