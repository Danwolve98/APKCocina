package com.example.apkcocina.utils.states

import com.example.apkcocina.utils.model.User
import com.google.firebase.auth.FirebaseUser

sealed class LoginResult(){
    data class Error (val error : String = "Generic error") : LoginResult()
    data class Logged(val user: FirebaseUser) : LoginResult()
    data object UnverifiedEmail : LoginResult()
    data object NoExistAccount : LoginResult()
    data object NoValidPassword : LoginResult()
    data object NotLogged : LoginResult()
}

sealed class RegisterResult(){
    data class Error (val error : String = "Generic error") : RegisterResult()
    data class Registered(val user: FirebaseUser) : RegisterResult()
    data object DuplicatedAccount : RegisterResult()
}

sealed class UpdateResult() {
    data class Error(val error: String = "Generic error") : UpdateResult()
    data class Updated(val user: User) : UpdateResult()
}

sealed class ResetPassWordResult(){
    data object Updated : ResetPassWordResult()
    data object EmailCredentialsFail : ResetPassWordResult()
    data object PasswordCredentialsFail : ResetPassWordResult()
}

class ProfileState(var isLoading: Boolean = false,
                   var isValidEmail: Boolean = true,
                   var isValidPassword: Pair<Boolean,String?> = Pair(true,null))