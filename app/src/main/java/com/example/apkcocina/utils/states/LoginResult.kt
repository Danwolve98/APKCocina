package com.example.apkcocina.utils.states

import com.google.firebase.auth.FirebaseUser

sealed class LoginResult(){
    data class Error (val error : String = "Generic error") : LoginResult()
    data class Logged(val user: FirebaseUser?) : LoginResult()
    object UnverifiedEmail : LoginResult()
    object NotLogged : LoginResult()
}

class ProfileState(var isLoading: Boolean = false,
                   var isValidEmail: Boolean = true,
                   var isValidPassword: Boolean = true)