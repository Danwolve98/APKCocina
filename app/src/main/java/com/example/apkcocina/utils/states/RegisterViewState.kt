package com.example.apkcocina.utils.states

sealed class RegisterViewState {
    data class Error(val error: String) : RegisterViewState()

}