package com.example.apkcocina.features.profile.viewModel

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.R
import com.example.apkcocina.network.usecases.CreateAccountUseCase
import com.example.apkcocina.network.usecases.LoginUseCase
import com.example.apkcocina.network.usecases.SendEmailVerificationUseCase
import com.example.apkcocina.network.usecases.VerifyEmailUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.ProfileState
import com.example.apkcocina.utils.states.RegisterResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val loginUseCase : LoginUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: CreateAccountUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _profileViewState = MutableStateFlow(ProfileState())
    val profileViewState: StateFlow<ProfileState> = _profileViewState

    private val _loginResult = MutableLiveData<Event<LoginResult.Logged>>()
    val loginResult: LiveData<Event<LoginResult.Logged>> = _loginResult

    private val _loginError = MutableLiveData<Event<String>>()
    val loginError: LiveData<Event<String>> = _loginError

    private val _registerError = MutableLiveData<Event<String>>()
    val registerError: LiveData<Event<String>> = _registerError

    private val _registerResult = MutableLiveData<Event<RegisterResult.Registered>>()
    val registerResult: LiveData<Event<RegisterResult.Registered>> = _registerResult

    private val _verifiedEmail = MutableLiveData<Event<Boolean>>()
    val verifiedEmail: LiveData<Event<Boolean>> = _verifiedEmail

    companion object{
        const val MIN_LONG_ENTRY = 6
    }

    fun login(email : String,contrasena : String) {
        if (isValidEmail(email) && isValidPassword(contrasena).first) {
            viewModelScope.launch() {
                _profileViewState.value= ProfileState(isLoading = true)
                when(val loginResult = loginUseCase(email,contrasena)){
                    is LoginResult.Logged -> _loginResult.value = Event(loginResult)
                    is LoginResult.Error -> _loginError.value = Event(loginResult.error)
                    is LoginResult.NoExistAccount->Event(context.getString(R.string.usuario_no_existe))
                    is LoginResult.NoValidPassword->Event(context.getString(R.string.email_o_contrasena_no_validos))
                    LoginResult.UnverifiedEmail -> sendEmailVerification()
                    else ->  _loginError.value = Event(context.getString(R.string.error_default))
                    }
                _profileViewState.value=ProfileState(isLoading = false)
            }
        }else{
            Log.e("error validacion","correo o contraseña no validos")
        }
    }

    fun register(email : String,contrasena : String){
        if (isValidEmail(email) && isValidPassword(contrasena).first) {
            viewModelScope.launch() {
                _profileViewState.value= ProfileState(isLoading = true)
                when(val registerResult = registerUseCase(email,contrasena)){
                    is RegisterResult.Registered->
                    {
                        _registerResult.value = Event(registerResult)
                        sendEmailVerification()
                    }
                    is RegisterResult.Error -> {
                        _registerError.value = Event(registerResult.error)
                        _profileViewState.value=ProfileState(isLoading = false)
                    }
                    RegisterResult.DuplicatedAccount-> {
                        _registerError.value = Event(context.getString(R.string.cuenta_ya_existente_inicie_sesion))
                        _profileViewState.value=ProfileState(isLoading = false)
                    }
                }
            }
        }else{
            Log.e("error validacion","correo o contraseña no validos")
        }
    }

    fun sendEmailVerification() =
        viewModelScope.launch() {
            sendEmailVerificationUseCase()
            withContext(Dispatchers.Main){
                Toast.makeText(context,context.getString(R.string.se_ha_enviado_un_email_de_verificacion_a_tu_correo),Toast.LENGTH_SHORT).show()
            }
            verifyEmailUseCase().catch {
                _verifiedEmail.value = Event(false)
            }.collect{verificado->
                if(verificado){
                    _verifiedEmail.value = Event(verificado)
                    _profileViewState.value=ProfileState(isLoading = false)
                    this.cancel()
                }
            }
        }

    fun onFieldsChanged(email: String, password: String) {
        _profileViewState.value = ProfileState(
            isValidEmail = isValidEmail(email),
            isValidPassword = isValidPassword(password)
        )
    }

    private fun isValidEmail(correo: String) =
        Patterns.EMAIL_ADDRESS.matcher(correo).matches() || correo.isEmpty()

    private fun isValidPassword(contrasena: String): Pair<Boolean,String?>{
        var errorText = context.getString(R.string.contrasena_no_es_valida)
        var valid = true

        if(contrasena.length < MIN_LONG_ENTRY || contrasena.isEmpty()){
            errorText = errorText.plus("\n\t"+context.getString(R.string.debe_tener_minimo_seis_caracteres))
            valid = false
        }

        if(!contrasena.any{it.isUpperCase()}){
            errorText = errorText.plus("\n\t"+context.getString(R.string.debe_contener_una_mayuscula))
            valid = false
        }

        if(!contrasena.any{it.isDigit()}){
            errorText = errorText.plus("\n\t"+context.getString(R.string.debe_contener_un_numero))
            valid = false
        }

        return Pair(valid,errorText)
    }


}