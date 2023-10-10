package com.example.apkcocina.network.usecases

import android.net.Uri
import com.example.apkcocina.network.services.FireBaseService
import com.example.apkcocina.utils.states.UpdateResult
import java.util.Date
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val fireBaseService : FireBaseService) {
    operator fun invoke(
        nombre: String? = null,
        apellidos : String? = null,
        nacionalidad:String? = null,
        cumpleanos : Date?=null,
        photoUri : Uri? = null): UpdateResult
        = fireBaseService.updateUser(nombre,apellidos,nacionalidad,cumpleanos,photoUri)

}