package com.example.apkcocina.features.recetasBase.repository

import android.content.Context
import com.example.apkcocina.data.model.Receta
import com.example.apkcocina.utils.db.APKCocinaDataBase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecetasBaseRespoitory @Inject constructor(
    val firebaseFirestore: FirebaseFirestore,
    val apkCocinaDataBase: APKCocinaDataBase
) {

    suspend fun getRecetas() : List<Receta>{
        var recetas = mutableListOf<Receta>()
        val recetasBaseDAO = apkCocinaDataBase.recetasBaseDAO()
        if(recetasBaseDAO.getAllRecetasBase().isEmpty()){
            firebaseFirestore.collection("recetas").get(Source.SERVER)
                .addOnSuccessListener {result->
                result.documentChanges
                for(r in result){
                    val receta = r.toObject<Receta>()
                    CoroutineScope(Dispatchers.IO).launch {
                        recetasBaseDAO.insertReceta(receta)
                    }
                    recetas.add(receta)
                }
            }
        } else{
            recetas = recetasBaseDAO.getAllRecetasBase().toMutableList()
        }
        return recetas
    }


}