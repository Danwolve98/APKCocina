package com.example.apkcocina.features.crearReceta.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.base64ToUri
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.model.Descripcion
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.SendRecetaState
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class CrearRecetasRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val storage : FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ICrearRecetas {
    override suspend fun sendReceta(receta: Receta): SendRecetaState {
        val user = firebaseAuth.currentUser
            ?: return SendRecetaState.Error(context.getString(R.string.usuario_no_existe))

        receta.usuario = user.uid
        val collection = firebaseFirestore.collection(Receta.RECETAS_USUARIOS)
        val timeMilis = Calendar.getInstance().time.time
        return runCatching {
            receta.descripcion = saveImages(timeMilis.toString(),receta.descripcion)
            if(receta.imagenPrincipal != null)
                receta.imagenPrincipal = saveImagePrincipal(timeMilis.toString(),receta.imagenPrincipal!!)
            collection.add(receta).await()
        }.fold(
            onSuccess = {
                    val documentId = mapOf(
                        "id" to it.id
                    )
                runCatching {
                    collection.document(it.id).update(documentId)
                }.fold(
                    onSuccess = {SendRecetaState.Successfull},
                    onFailure = {SendRecetaState.Error(context.getString(R.string.error_al_crear_la_receta))}
                )
                        },
            onFailure = {
                SendRecetaState.Error(context.getString(R.string.error_al_crear_la_receta))
            }
        )
    }

    private suspend fun saveImagePrincipal(idFolder : String,imagenPrincipal: String) : String?{
        val referencia = storage.reference.child("images/${firebaseAuth.currentUser!!.uid}/$idFolder/mainImage")
        return runCatching {
            referencia.putFile(imagenPrincipal.base64ToUri(context)!!).await()
        }.fold(
            onSuccess = {
                runCatching {
                    it.storage.downloadUrl.await()
                }   .fold(
                    onSuccess = {
                        it.toString()
                    },
                    onFailure = {
                        null
                    }
                )
            },
            onFailure = {
                null
            }
        )
    }



    private suspend fun saveImages(idFolder : String, recetaDescripcion: List<Descripcion>?): List<Descripcion>?
    {
        return if (recetaDescripcion != null) {
            var contador = 0
            val tasks = recetaDescripcion
                    .filter { it.image }
                    .map { descripcion ->
                        contador++
                        val referencia = storage.reference.child("images/${firebaseAuth.currentUser!!.uid}/$idFolder/${String.format("%03d",contador)}")
                        referencia.putFile(descripcion.string.base64ToUri(context)!!).continueWithTask{ task->
                            if(task.isSuccessful){
                                referencia.downloadUrl
                            }else{
                               null
                            }
                        }
                    }
            val descripcionUrls = Tasks.whenAllSuccess<Uri?>(tasks).await()
            recetaDescripcion.map {
                if(it.image)
                    Descripcion(descripcionUrls.removeAt(0).toString(),true)
                else
                    it
            }
        }else{
            null
        }
    }
}

interface ICrearRecetas{
    suspend fun sendReceta(receta : Receta) : SendRecetaState
}