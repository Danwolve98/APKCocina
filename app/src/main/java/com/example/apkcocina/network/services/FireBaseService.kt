package com.example.apkcocina.network.services

import android.content.Context
import android.net.Uri
import com.example.apkcocina.R
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.extensions.getUri
import com.example.apkcocina.utils.extensions.notNullorDefault
import com.example.apkcocina.utils.model.User
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.RegisterResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.Date
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
            return when(it){
                is FirebaseAuthInvalidCredentialsException-> LoginResult.NoValidPassword
                is FirebaseAuthInvalidUserException->LoginResult.NoExistAccount
                else->LoginResult.NotLogged
            }
        }.toLoginResult()

    suspend fun createAccount(email: String, password: String) : RegisterResult =
        runCatching {
            auth.createUserWithEmailAndPassword(email, password).await()
        }.onFailure {
            return when(it){
                is FirebaseAuthUserCollisionException-> RegisterResult.DuplicatedAccount
                else -> {RegisterResult.Error(it.message.toString())}
            }
        }.toRegisterResult()

    /*suspend fun updateUser(nombre: String? = null, apellidos : String? = null, nacionalidad:String? = null, cumpleanos : Date?=null, photoUri : Uri? = null) : Pair<Boolean,String> {
        if (auth.currentUser != null) {
            if(!updateUserAuth(nombre,photoUri))
                return Pair(false,context.getString(R.string.error_update_profile))

            val document = store.collection(User.USUARIOS).document(auth.currentUser!!.uid).get().addOnSuccessListener {
                val usuario = it as User
            }.addOnFailureListener{

            }
        }
        else {
            false
        }
    }*/

    fun updateUserAuth(name: String? = null, photoUri: Uri? = null) : Boolean {
        var successful = false
        val requestUpdateUser = UserProfileChangeRequest.Builder()
            .setDisplayName(
                name.notNullorDefault(notNullAction =  { return@notNullorDefault name },
                    nullAction = { return@notNullorDefault auth.currentUser?.displayName.notNullorDefault(context.getString(R.string.chef_curioso)) })
            )
            .setPhotoUri(
                photoUri.notNullorDefault(notNullAction =  { return@notNullorDefault photoUri },
                nullAction = { return@notNullorDefault auth.currentUser?.photoUrl.notNullorDefault(context.getDrawable(R.drawable.ic_chef)?.getUri())})
            )
            .build()

        auth.currentUser!!.updateProfile(requestUpdateUser).addOnSuccessListener {
            File(context.cacheDir, Constants.PROFILE_IMAGES_CACHE).deleteRecursively()
            successful = true
        }
        return successful
    }


    suspend fun sendVerificationEmail() = runCatching {
        auth.currentUser?.sendEmailVerification()?.await()
    }.isSuccess

    private suspend fun verifyEmailIsVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified ?: false
    }

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error(context.getString(R.string.error_login))
        else -> {
            checkNotNull(result.user)
            if(result.user!!.isEmailVerified)
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
            store.collection(User.USUARIOS).document(user.uid).set(User(user.displayName!!))
            RegisterResult.Registered(user)
        }
    }

}