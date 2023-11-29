package com.example.apkcocina.utils.states

sealed class CrearRecetaState {
    data object Successfull : CrearRecetaState()
    data class Error(val error : String) : CrearRecetaState()
}