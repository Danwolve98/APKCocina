package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authenticationService: FireBaseService
) {
    suspend operator fun invoke(email:String,constrasena:String): Boolean =
        authenticationService.createAccount(email,constrasena) != null
}