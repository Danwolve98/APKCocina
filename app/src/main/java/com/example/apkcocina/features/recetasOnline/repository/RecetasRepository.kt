package com.example.apkcocina.features.recetasOnline.repository

import android.content.Context
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.model.User
import com.example.apkcocina.utils.states.RandomRecetaState
import com.example.apkcocina.utils.states.RecetaState
import com.example.apkcocina.utils.states.RecetasOnlineState
import com.example.apkcocina.utils.states.SetRecetaFavState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecetasRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : IRecetas {

    override suspend fun getRecetas(): RecetasOnlineState =
        runCatching {
            firestore.collection(Receta.RECETAS_USUARIOS).get().await()
        }.fold(
            onSuccess = {
                val listRecetas = it.toObjects(Receta::class.java)
                RecetasOnlineState.Successfull(listRecetas)
            },
            onFailure = {
                RecetasOnlineState.Error(context.getString(R.string.error_en_el_servidor))
            }
        )

    override suspend fun getReceta(id: String,collection : String): RecetaState =
        runCatching {
            firestore.collection(collection).document(id).get().await()
        }.fold(
            onSuccess = {
                val receta = it.toObject(Receta::class.java) ?: return@fold RecetaState.Error(context.getString(R.string.error_en_el_servidor))
                //ESTA PARTE DE AQUÍ ES PARA SABER SI LA RECETA SACADA EL USUARIO LA TIENE EN FAVORITOS
                if(auth.currentUser != null) {
                    val user = firestore.collection(User.USUARIOS).document(auth.currentUser!!.uid).get().await()
                    val recestasFav = user.toObject(User::class.java)!!.recetasFav
                    RecetaState.Successfull(receta,recestasFav?.contains(receta.id) ?: false)
                } else{
                    RecetaState.Successfull(receta)
                }
            },
            onFailure = {
                RecetaState.Error(context.getString(R.string.error_en_el_servidor))
            }
        )

    override suspend fun getRecetasUser(): RecetasOnlineState =
        runCatching {
            firestore.collection(Receta.RECETAS_USUARIOS).whereEqualTo("usuario",auth.currentUser!!.uid).get().await()
        }.fold(
            onSuccess = {
                val recetasUsuario = it.toObjects(Receta::class.java)
                RecetasOnlineState.Successfull(recetasUsuario)
                        },
            onFailure = {RecetasOnlineState.Error(context.getString(R.string.no_se_han_encontrado_recetas))}
        )

    override suspend fun getRecetaAletatoria(): RandomRecetaState =
        runCatching {
            firestore.collection(Receta.RECETAS_USUARIOS).get().await()
        }.fold(
            onSuccess = {
                if(it.size()>0){
                    val recetaAleatoriaNum = (Math.random() * it.size()).toInt()
                    val recetaRandom : Receta = it.documents[recetaAleatoriaNum].toObject(Receta::class.java)!!
                    RandomRecetaState.Successfull(recetaRandom.id,recetaRandom.nombre ?: context.getString(R.string.unkown))
                }else
                    RandomRecetaState.Error(context.getString(R.string.no_se_han_encontrado_recetas))
            },
            onFailure = {RandomRecetaState.Error(context.getString(R.string.error_en_el_servidor))}
        )

    override suspend fun setRecetaFav(recetaId : String): SetRecetaFavState =
        runCatching {
            firestore.collection(User.USUARIOS).document(auth.currentUser!!.uid).update("recetasFav",FieldValue.arrayUnion(recetaId)).await()
        }.fold(
            onSuccess = {
                SetRecetaFavState.Successfull
            },
            onFailure = {
                SetRecetaFavState.Error(context.getString(R.string.no_se_pudo_guardar_en_favoritos))
            }
        )

    override suspend fun removeRecetaFav(recetaId: String): SetRecetaFavState =
        runCatching {
            firestore.collection(User.USUARIOS).document(auth.currentUser!!.uid).update("recetasFav",FieldValue.arrayRemove(recetaId)).await()
        }.fold(
            onSuccess = {
                SetRecetaFavState.Successfull
            },
            onFailure = {
                SetRecetaFavState.Error(context.getString(R.string.error_en_el_servidor))
            }
        )

    override suspend fun getRecetasFavUser(): RecetasOnlineState =
        runCatching {
            firestore.collection(User.USUARIOS).document(auth.currentUser!!.uid).get().await()
        }.fold(
            onSuccess = {document->
                val user = document.toObject(User::class.java)!!
                runCatching {
                    firestore.collection(Receta.RECETAS_USUARIOS).whereIn(FieldPath.documentId(),user.recetasFav!!).get().await()
                }.fold(
                    onSuccess = {query->
                        val recetasFavUser = query.toObjects(Receta::class.java)
                        RecetasOnlineState.Successfull(recetasFavUser)
                    },
                    onFailure = {
                        RecetasOnlineState.SinRecetasFav
                    }
                )
            },
            onFailure = {
                RecetasOnlineState.Error(context.getString(R.string.error_en_el_servidor))
            }
        )


}




interface IRecetas{
    suspend fun getRecetas() : RecetasOnlineState
    suspend fun getReceta(id : String,collection : String) : RecetaState
    suspend fun getRecetasUser() : RecetasOnlineState
    suspend fun getRecetaAletatoria() : RandomRecetaState
    suspend fun setRecetaFav(recetaId : String) : SetRecetaFavState
    suspend fun removeRecetaFav(recetaId : String) : SetRecetaFavState
    suspend fun getRecetasFavUser() : RecetasOnlineState
}