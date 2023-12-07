package com.example.apkcocina.features.recetasOnline.repository

import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.RecetasOnlineState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecetasRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : IRecetas {

    override suspend fun getRecetas() : RecetasOnlineState =
        runCatching {
            firestore.collection(Receta.RECETAS_USUARIOS).get().await()
        }.fold(
            onSuccess = {
                val listRecetas = it.toObjects(Receta::class.java)
                RecetasOnlineState.Successfull(listRecetas)
            },
            onFailure = {
                RecetasOnlineState.Error("Error con el servidor")
            }
        )

}



interface IRecetas{
    suspend fun getRecetas() : RecetasOnlineState
}