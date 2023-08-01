package com.example.apkcocina.network.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseService @Inject constructor(private val auth: FirebaseAuth) {

    val verifiedAccount: Flow<Boolean> = flow {
        while (true) {
            val verified = verifyEmailIsVerified()
            emit(verified)
            delay(2000)
        }
    }

    suspend fun login(email: String, password: String): Boolean = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
    }.toLoginResult()

    suspend fun createAccount(email: String, password: String): AuthResult? {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun sendVerificationEmail() = runCatching {
        auth.currentUser?.sendEmailVerification()?.await()
    }.isSuccess

    private suspend fun verifyEmailIsVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified ?: false
    }

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> false
        else -> {
            checkNotNull(result.user)
            result.user!!.isEmailVerified
        }
    }


}