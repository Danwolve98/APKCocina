package com.example.apkcocina.features.recetasBase.repository

import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.db.APKCocinaDataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecetasDataBaseRespoitory @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val apkCocinaDataBase: APKCocinaDataBase
) {

    suspend fun getRecetas() : List<Receta>{
        var recetas = mutableListOf<Receta>()
        val recetasDataBaseDAO = apkCocinaDataBase.recetasBaseDAO()
        if(recetasDataBaseDAO.getAllRecetasBase().isEmpty()){
            firebaseFirestore.collection(Receta.RECETAS_BASE).get(Source.SERVER)
                .addOnSuccessListener {result->
                result.documentChanges
                for(r in result){
                    val receta = r.toObject<Receta>()
                    CoroutineScope(Dispatchers.IO).launch {
                        recetasDataBaseDAO.insertReceta(receta)
                    }
                    recetas.add(receta)
                }
            }
        } else{
            recetas = recetasDataBaseDAO.getAllRecetasBase().toMutableList()
        }
        return recetas
    }

    suspend fun insertRecetaFav(recetaId : String){

    }

    suspend fun deleteRecetaFav(recetaId : String){

    }

}