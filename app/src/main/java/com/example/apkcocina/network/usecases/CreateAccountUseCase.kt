package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import com.example.apkcocina.utils.states.RegisterResult
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authenticationService: FireBaseService
) {
    suspend operator fun invoke(email:String,constrasena:String): RegisterResult =
        authenticationService.createAccount(email,constrasena)
}