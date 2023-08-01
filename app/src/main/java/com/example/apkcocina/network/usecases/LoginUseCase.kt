package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val fireBaseService: FireBaseService) {

    suspend operator fun invoke(email: String, contrasena: String): Boolean =
        fireBaseService.login(email, contrasena)
}