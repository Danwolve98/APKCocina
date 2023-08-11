package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import com.example.apkcocina.utils.states.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val fireBaseService: FireBaseService) {

    suspend operator fun invoke(email: String, contrasena: String): LoginResult =
        fireBaseService.login(email, contrasena)
}