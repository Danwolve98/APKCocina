package com.example.apkcocina.features.profile.viewModel

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.right
import com.example.apkcocina.network.usecases.CreateAccountUseCase
import com.example.apkcocina.network.usecases.LoginUseCase
import com.example.apkcocina.network.usecases.SendEmailVerificationUseCase
import com.example.apkcocina.network.usecases.VerifyEmailUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUseCase : LoginUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: CreateAccountUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _profileViewState = MutableStateFlow(ProfileState())
    val profileViewState: StateFlow<ProfileState> = _profileViewState

    private val _loginResult = MutableStateFlow(Event(LoginResult.Logged(null)))
    val loginResult: StateFlow<Event<LoginResult.Logged>> = _loginResult

    private val _loginError = MutableLiveData<Event<Boolean>>()
    val loginError: LiveData<Event<Boolean>>
        get() = _loginError

    private val _loginVerifiedEmail = MutableLiveData<Event<Boolean>>()
    val loginVerifiedEmail: LiveData<Event<Boolean>>
        get() = _loginVerifiedEmail

    companion object{
        const val MIN_LONG_ENTRY = 6
    }

    fun login(email : String,contrasena : String) {
        if (isValidEmail(email) && isValidPassword(contrasena)) {
            _profileViewState.value= ProfileState(isLoading = true)
            viewModelScope.launch {
                when(val loginResult = loginUseCase(email,contrasena)){
                    is LoginResult.Logged -> _loginResult.value = Event(loginResult)
                    is LoginResult.Error -> _loginError.value = Event(true)
                    LoginResult.UnverifiedEmail -> _loginVerifiedEmail.value = Event(false)
                    else ->  _loginError.value = Event(true)
                    }
            }
            _profileViewState.value=ProfileState(isLoading = false)
            LoginResult.Logged(null).right()
        }else{
            Log.e("error validacion","correo o contraseña no validos")
        }
    }

   /* fun register(email : String,contrasena : String) : Either<String?,Boolean> {
        return if (isValidEmailPassw(email, contrasena) != null) {
            viewModelScope.async {
                registerUseCase.invoke(email, contrasena)
            }.isCompleted.right()
        }else{
            isValidEmailPassw(email, contrasena).left()
        }
    }*/

    private fun isValidEmail(correo: String) =
        Patterns.EMAIL_ADDRESS.matcher(correo).matches() || correo.isEmpty()

    private fun isValidPassword(contrasena: String): Boolean =
        contrasena.length >= MIN_LONG_ENTRY || contrasena.isEmpty()

}