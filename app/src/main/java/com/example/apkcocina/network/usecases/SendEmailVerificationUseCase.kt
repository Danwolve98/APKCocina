package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(private val fireBaseService: FireBaseService) {

    suspend operator fun invoke() = fireBaseService.sendVerificationEmail()
}