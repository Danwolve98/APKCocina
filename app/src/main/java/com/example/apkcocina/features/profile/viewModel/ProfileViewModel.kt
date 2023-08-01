package com.example.apkcocina.features.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.apkcocina.network.usecases.CreateAccountUseCase
import com.example.apkcocina.network.usecases.LoginUseCase
import com.example.apkcocina.network.usecases.SendEmailVerificationUseCase
import com.example.apkcocina.network.usecases.VerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUseCase : LoginUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: CreateAccountUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {
    companion object{
        const val MIN_LONG_ENTRY = 6
        val EMAIL_REGEX = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val UPPERCASE = Regex("[A-Z]")
        val SPECIAL_CHARACTER = Regex("[^A-Za-z0-9]")
    }


    fun loginClick(email : String,contrasena : String) : Either<String?,Boolean> {
        return if (isValidEmailPassw(email, contrasena) != null) {
            viewModelScope.async {
                loginUseCase.invoke(email, contrasena)
            }.isCompleted.right()
        }else{
            isValidEmailPassw(email, contrasena).left()
        }
    }

    private fun isValidEmailPassw(email: String, contrasena: String) : String? =
        if(!email.matches(EMAIL_REGEX)){
            "Escribe un email valido"
        }else if(contrasena.length < MIN_LONG_ENTRY){
            "Contraseña demasiado corta"
        }else if(!UPPERCASE.containsMatchIn(contrasena)){
            "La contraseña debe incluir al menos una mayúscula"
        }else if(contrasena.contains(UPPERCASE)){
            "La contraseña debe incluir al menos una mayúscula"
        }else if(SPECIAL_CHARACTER.containsMatchIn(contrasena)){
            "La contraseña debe contener un carácter especial"
        }else{
            null
        }


}