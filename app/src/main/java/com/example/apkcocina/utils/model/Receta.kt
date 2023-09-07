package com.example.apkcocina.utils.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable
@Entity(tableName = "recetas")
class Receta(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    var nombre : String? = null,
    var tiempoPreparacion : Int? = null,
    var alergenos: List<Alergenos>? = null,
    var descripcion : String? = null,
    var imagenes : List<String>? = null,
    var ingredientes : HashMap<String,String>? = null
): Serializable{
    companion object{
        val RECETAS_BASE = "recetas"
        val RECETAS_USUARIOS = "recetasUsuarios"
    }
    fun getReferencia() : String =
        if(!imagenes.isNullOrEmpty())
            "${imagenes!![0]}/${imagenes!![0]}.jpg"
        else
            ""

}