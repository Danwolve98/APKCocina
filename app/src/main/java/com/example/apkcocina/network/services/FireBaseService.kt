package com.example.apkcocina.network.services

import android.content.Context
import arrow.core.left
import com.example.apkcocina.R
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.RegisterResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseService @Inject constructor(
    @ApplicationContext val context: Context,
    private val auth: FirebaseAuth
    ) {

    val verifiedAccount: Flow<Boolean> = flow {
        while (true) {
            val verified = verifyEmailIsVerified()
            emit(verified)
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
            checkNotNull(result.user)
            RegisterResult.Registered(result.user!!)
        }
    }

}