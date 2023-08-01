package com.example.apkcocina.network.usecases

import com.example.apkcocina.network.services.FireBaseService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(private val fireBaseService : FireBaseService) {

    operator fun invoke(): Flow<Boolean> = fireBaseService.verifiedAccount

}