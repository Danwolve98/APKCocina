package com.example.apkcocina.network.services

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.apkcocina.R
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.extensions.getUri
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.notNullorDefault
import com.example.apkcocina.utils.extensions.toBase64
import com.example.apkcocina.utils.model.User
import com.example.apkcocina.utils.states.CargarUserResult
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.ReauthenticateResult
import com.example.apkcocina.utils.states.RegisterResult
import com.example.apkcocina.utils.states.ResetPassWordResult
import com.example.apkcocina.utils.states.UpdateResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseService @Inject constructor(
    @ApplicationContext val context: Context,
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) {

    val verifiedAccount: Flow<Boolean> = flow {
        var verifiedEmail = false
        while (!verifiedEmail) {
            verifiedEmail = verifyEmailIsVerified()
            emit(verifiedEmail)
            delay(2000)
        }
    }

    suspend fun login(email: String, password: String): LoginResult =
        runCatching {
            auth.signInWithEmailAndPassword(email, password).await()
        }.onFailure {
            return when (it) {
                is FirebaseAuthInvalidCredentialsException -> LoginResult.NoValidPassword
                is FirebaseAuthInvalidUserException -> LoginResult.NoExistAccount
                else -> LoginResult.NotLogged
            }
        }.toLoginResult()

    suspend fun createAccount(email: String, password: String): RegisterResult =
        runCatching {
            auth.createUserWithEmailAndPassword(email, password).await()
        }.onFailure {
            return when (it) {
                is FirebaseAuthUserCollisionException -> RegisterResult.DuplicatedAccount
                else -> {
                    RegisterResult.Error(it.message.toString())
                }
            }
        }.toRegisterResult()

    suspend fun updateUser(
        nombre: String? = null,
        apellidos: String? = null,
        nacionalidad: String? = null,
        cumpleanos: Calendar? = null,
        photo: String? = null
    ): UpdateResult {
        var updateResult: UpdateResult = UpdateResult.Error()
        if (auth.currentUser != null) {

                val document = store.collection(User.USUARIOS).document(auth.currentUser!!.uid)
                document.get().addOnSuccessListener { documentSnapshot ->
                        val usuario = documentSnapshot.toObject(User::class.java)
                        if (usuario != null) {
                            nombre.notNull { usuario.nombre = it }
                            apellidos.notNull { usuario.apellidos = it }
                            nacionalidad.notNull { usuario.nacionalidad = it }
                            cumpleanos.notNull { usuario.cumpleanos = it.timeInMillis }
                            photo.notNull { usuario.foto = it }
                            document.set(usuario)
                                .addOnSuccessListener {
                                    updateResult = UpdateResult.Updated(usuario)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "ERROR AL ACTUALIZAR USUARIO",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        UpdateResult.Error(context.getString(R.string.no_se_ha_podido_recoger_informacion_del_usuario))
                    }.await()
        } else {
            updateResult = UpdateResult.Error(context.getString(R.string.usuario_no_existe))
        }

        return updateResult
    }

    @Deprecated("MÉTODO QUE SE USABA PARA ACTUALIZAR LA FOTO DE UN USUARIO A NIVEL DE AUTHENTIFICATOR DE FIREBASE, PERO DEBIDO A LA POCA SEGURIDAD DE USAR URIS SE CAMBIO A GUARDAR EN " +
            "BASE64 EN BASE DE DATOS")
    private suspend fun updateUserAuth(photoUri: Uri? = null): Boolean {
        val requestUpdateUser = UserProfileChangeRequest.Builder()
            .setPhotoUri(
                photoUri.notNullorDefault(
                    notNullAction = { return@notNullorDefault photoUri },
                    nullAction = {
                        return@notNullorDefault auth.currentUser?.photoUrl.notNullorDefault(context.getDrawable(R.drawable.ic_chef)?.getUri(context)
                        )
                    })
            )
            .build()
        return runCatching {
            auth.currentUser!!.updateProfile(requestUpdateUser).await()
        }.fold(
            onSuccess = {
                File(context.cacheDir, Constants.PROFILE_IMAGES_CACHE).deleteRecursively()
                true
            },
            onFailure = {
                false
            }
        )
    }

    suspend fun sendVerificationEmail() = runCatching {
        auth.currentUser?.sendEmailVerification()?.await()
    }.isSuccess

    private suspend fun verifyEmailIsVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified ?: false
    }

    //TODO cambiar esto
    suspend fun sendResetPasswordEmail(email : String, oldPassword : String, newPassword : String) : ResetPassWordResult{
        var resetPassState : ResetPassWordResult = ResetPassWordResult.GenericError(context.getString(R.string.error_default))
        val credential = EmailAuthProvider.getCredential(email,oldPassword)

        when(val resultReauthenticate = reauthenticateUser(credential)){
            ReauthenticateResult.Authenticated -> {
               val updatePassword =  auth.currentUser?.updatePassword(newPassword)
                    ?.addOnSuccessListener{
                        resetPassState = ResetPassWordResult.Updated
                    }
                    ?.addOnFailureListener {exception->
                        resetPassState = when(exception) {
                            is FirebaseTooManyRequestsException -> ResetPassWordResult.TooManyTrys
                            is FirebaseNetworkException -> ResetPassWordResult.NetWorkProblem
                            else -> ResetPassWordResult.GenericError(context.getString(R.string.error_default))
                        }
                    }
                updatePassword?.await()
            }
            is ReauthenticateResult.GenericError ->{
                resetPassState = ResetPassWordResult.GenericError(resultReauthenticate.error)
            }
        }
        return resetPassState
    }

    private suspend fun reauthenticateUser(credential : AuthCredential) : ReauthenticateResult =
        runCatching {
            auth.currentUser?.reauthenticate(credential)?.await()
        }.fold(
            onSuccess = {
                ReauthenticateResult.Authenticated
            },
            onFailure = {
                when(it){
                    is FirebaseAuthInvalidUserException-> ReauthenticateResult.GenericError(context.getString(R.string.cuenta_no_existente))
                    is FirebaseAuthInvalidCredentialsException-> ReauthenticateResult.GenericError(context.getString(R.string.error_al_autentificar_el_usuario))
                    else -> ReauthenticateResult.GenericError(context.getString(R.string.error_default))
                }
            }
        )

    suspend fun cargarUsuario() : CargarUserResult =
        runCatching {
            store.collection(User.USUARIOS).document(auth.currentUser?.uid ?: "").get().await()
        }.fold(
            onSuccess = {
                val user = it.toObject(User::class.java)
                if(user != null)
                    CargarUserResult.Successfull(User(auth.currentUser!!.uid,
                        user.nombre,
                        user.apellidos,
                        user.recetas,
                        user.nacionalidad,
                        user.foto,
                        user.cumpleanos,
                        user.fechaDeRegistro))
                else
                    CargarUserResult.Error
            },
            onFailure = {
                CargarUserResult.Error
            }
        )

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error(context.getString(R.string.error_login))
        else -> {
            checkNotNull(result.user)
            if (result.user!!.isEmailVerified)
                LoginResult.Logged(result.user!!)
            else
                LoginResult.UnverifiedEmail
        }
    }

    private fun Result<AuthResult>.toRegisterResult() = when (val result = getOrNull()) {
        null -> RegisterResult.Error(context.getString(R.string.error_login))
        else -> {
            val user = result.user
            checkNotNull(user)
            store.collection(User.USUARIOS).document(user.uid).set(
                User(
                    user.uid,
                    context.getString(R.string.chef),
                    context.getString(R.string.curioso),
                    fechaDeRegistro = Calendar.getInstance().timeInMillis
                )
            )
            RegisterResult.Registered(user)
        }
    }
}