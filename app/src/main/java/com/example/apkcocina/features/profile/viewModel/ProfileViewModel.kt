package com.example.apkcocina.features.profile.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.R
import com.example.apkcocina.network.usecases.CargarUsuarioUseCase
import com.example.apkcocina.network.usecases.CreateAccountUseCase
import com.example.apkcocina.network.usecases.LoginUseCase
import com.example.apkcocina.network.usecases.ResetPassWordUserCase
import com.example.apkcocina.network.usecases.SendEmailVerificationUseCase
import com.example.apkcocina.network.usecases.UpdateUserUseCase
import com.example.apkcocina.network.usecases.VerifyEmailUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.model.User
import com.example.apkcocina.utils.states.CargarUserResult
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.ProfileState
import com.example.apkcocina.utils.states.RegisterResult
import com.example.apkcocina.utils.states.ResetPassWordResult
import com.example.apkcocina.utils.states.UpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context : Context,
    private val loginUseCase : LoginUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: CreateAccountUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val resetPassWordUserCase: ResetPassWordUserCase,
    private val cargarUsuarioUseCase: CargarUsuarioUseCase
) : ViewModel() {

    private val _profileViewState = MutableStateFlow(ProfileState())
    val profileViewState: StateFlow<ProfileState> = _profileViewState

    private val _loginResult = MutableLiveData<Event<LoginResult.Logged>>()
    val loginResult: LiveData<Event<LoginResult.Logged>> = _loginResult

    private val _updateResult = MutableLiveData<Event<UpdateResult>>()
    val updateResult: LiveData<Event<UpdateResult>> = _updateResult

    private val _loginError = MutableLiveData<Event<String>>()
    val loginError: LiveData<Event<String>> = _loginError

    private val _registerError = MutableLiveData<Event<String>>()
    val registerError: LiveData<Event<String>> = _registerError

    private val _registerResult = MutableLiveData<Event<RegisterResult.Registered>>()
    val registerResult: LiveData<Event<RegisterResult.Registered>> = _registerResult

    private val _verifiedEmail = MutableLiveData<Event<Boolean>>()
    val verifiedEmail: LiveData<Event<Boolean>> = _verifiedEmail

    private val _cargaUsuario = MutableLiveData<Event<User?>>()
    val cargaUsuario: LiveData<Event<User?>> = _cargaUsuario

    private var _resetEmailSent = MutableLiveData<Event<ResetPassWordResult>>()
    val resetEmailSent: LiveData<Event<ResetPassWordResult>> = _resetEmailSent

    companion object{
        const val MIN_LONG_ENTRY = 6
    }

    /**
     * Administra el login de un usuario
     *
     * @param email el email
     * @param contrasena la contraseña
     */
    fun login(email : String,contrasena : String) {
        if (isValidEmail(email) && isValidPassword(contrasena).first) {
            viewModelScope.launch() {
                _profileViewState.value= ProfileState(isLoading = true)
                when(val loginResult = loginUseCase(email,contrasena)){
                    is LoginResult.Logged -> _loginResult.value = Event(loginResult)
                    is LoginResult.Error -> _loginError.value = Event(loginResult.error)
                    is LoginResult.NoExistAccount->_loginError.value = Event(context.getString(R.string.usuario_no_existe))
                    is LoginResult.NoValidPassword-> _loginError.value = Event(context.getString(R.string.email_o_contrasena_no_validos))
                    LoginResult.UnverifiedEmail -> sendEmailVerification()
                    else ->  _loginError.value = Event(context.getString(R.string.error_default))
                }
                _profileViewState.value=ProfileState(isLoading = false)
            }
        }else{
            Log.e("error validacion","correo o contraseña no validos")
        }
    }

    /**
     * Registra un usuario nuevo
     *
     * @param email correo del usuario
     * @param contrasena la contraseña del mismo
     */
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

    /**
     * Función que se va a encargar de actualizar los datos de un usuario
     */
    fun updateUser(
        nombre: String? = null,
        apellidos : String? = null,
        nacionalidad:String? = null,
        cumpleanos : Calendar?=null,
        photo : String? = null) {
        viewModelScope.launch {
            _updateResult.postValue(Event(updateUserUseCase.invoke(nombre,apellidos,nacionalidad,cumpleanos,photo)))
        }
    }

    /**
     * Función que se va a encargar de mandar un email de verificación de cuenta al usuario existente
     */
    private fun sendEmailVerification() =
        viewModelScope.launch() {
            sendEmailVerificationUseCase()
            withContext(Dispatchers.Main){
                Toast.makeText(context,context.getString(R.string.se_ha_enviado_un_email_de_verificacion_a_tu_correo),Toast.LENGTH_SHORT).show()
            }
            verifyEmailUseCase().catch {
                _verifiedEmail.value = Event(false)
            }.collect{verificado->
                if(verificado){
                    _verifiedEmail.value = Event(true)
                    _profileViewState.value = ProfileState(isLoading = false)
                    this.cancel()
                }
            }
        }

    /**
     * Función que se va a encargar del reset de contraseña por parte del usuario
     *
     * @param email email de la cuenta para verificar
     * @param oldPassword contraseña antigua a validar usuario
     * @param newPassword contraseña nueva introducida
     */
    fun sendResetPasswordEmail(email : String,oldPassword : String,newPassword : String) =
        viewModelScope.launch {
            _profileViewState.value = ProfileState(isLoading = true)
            if(isValidEmail(email))
                _resetEmailSent.value = Event(resetPassWordUserCase(email,oldPassword,newPassword))
            _profileViewState.value = ProfileState(isLoading = false)
        }

    /**
     * Función que se va a ejecutar para hacer una validacion de correo y contraseña cada vez que cambien los campos en los textos
     *
     * @param email el email a comprobar
     * @param password la contraseña a comprobar
     */
    fun onFieldsChanged(email: String, password: String) {
        _profileViewState.value = ProfileState(
            isValidEmail = isValidEmail(email),
            isValidPassword = isValidPassword(password)
        )
    }

    /**
     * Comprueba si el correo es válido
     * @param correo el correo a comprobar
     */
    private fun isValidEmail(correo: String) = Patterns.EMAIL_ADDRESS.matcher(correo).matches() || correo.isEmpty()

    /**
     * Comprueba si la contraseña cumple con los requisitos mínimos:
     *   1. Una mayúscula mínimo
     *   2. Un dígito mínimo
     *   3. Una longitud mínima de 6 caracteres
     * @param contrasena la contraseña a comprobar
     * @return [Pair] donde el primer par es un [Boolean] si cumple o no y el segundo, en caso de no haber cumplido, con que cosas no ha cumplido en [String]
     */
    private fun isValidPassword(contrasena: String): Pair<Boolean,String?>{
        var errorText = context.getString(R.string.contrasena_no_es_valida)
        var valid = true
        if(contrasena.isEmpty())
            return Pair(valid,null)

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

    /**
     * Carga los datos del actual usuario
     */
    fun cargarUser(){
        viewModelScope.launch {
            _profileViewState.value = ProfileState(isLoading = true)
            when(val result = cargarUsuarioUseCase()){
                is CargarUserResult.Successfull -> _cargaUsuario.value = Event(result.user)
                    CargarUserResult.Error -> _cargaUsuario.value = Event(null)
            }
            _profileViewState.value = ProfileState(isLoading = false)
        }

    }


}