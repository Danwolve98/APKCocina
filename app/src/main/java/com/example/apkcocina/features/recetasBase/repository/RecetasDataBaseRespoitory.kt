package com.example.apkcocina.features.recetasBase.repository

import android.content.Context
import arrow.core.constant
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.db.APKCocinaDataBase
import com.example.apkcocina.utils.states.RecetaState
import com.example.apkcocina.utils.states.RecetasOnlineState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecetasDataBaseRespoitory @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseFirestore: FirebaseFirestore,
    private val apkCocinaDataBase: APKCocinaDataBase
) {
    suspend fun getRecetas() : RecetasOnlineState =
        runCatching {
            firebaseFirestore.collection(Receta.RECETAS_BASE).get().await()
        }.fold(
            onSuccess = {
                val recetas = it.toObjects(Receta::class.java)!!
                RecetasOnlineState.Successfull(recetas)
            },
            onFailure = {
                RecetasOnlineState.Error(context.getString(R.string.error_en_el_servidor))
            }
        )
}