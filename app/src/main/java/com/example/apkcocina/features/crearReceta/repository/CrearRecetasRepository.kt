package com.example.apkcocina.features.crearReceta.repository


import android.content.Context
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.CrearRecetaState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CrearRecetasRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ICrearRecetas {
    override suspend fun sendReceta(receta: Receta) : CrearRecetaState {
        val user = firebaseAuth.currentUser ?: return CrearRecetaState.Error(context.getString(R.string.usuario_no_existe))

        receta.usuario = user.uid

        val collection = firebaseFirestore.collection(Receta.RECETAS_USUARIOS)

        return runCatching {
            collection.add(receta).await()
        }.fold(
            onSuccess = {CrearRecetaState.Successfull},
            onFailure = {CrearRecetaState.Error(context.getString(R.string.error_al_crear_la_receta))}
        )
    }
}

interface ICrearRecetas{
    suspend fun sendReceta(receta : Receta) : CrearRecetaState
}