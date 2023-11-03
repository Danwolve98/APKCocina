package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import com.example.apkcocina.utils.states.ResetPassWordResult
import javax.inject.Inject

class ResetPassWordUserCase @Inject constructor(private val fireBaseService : FireBaseService) {
    suspend operator fun invoke(email : String): ResetPassWordResult = fireBaseService.sendResetPasswordEmail(email)
}