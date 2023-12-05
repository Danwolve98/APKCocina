package com.example.apkcocina.utils.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.errorprone.annotations.Keep
import java.io.Serializable
@Keep
@Entity(tableName = "recetas")
class Receta(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    var usuario : String? = null,
    var nombre : String? = null,
    var tiempoPreparacion : Int? = null,
    var alergenos: List<Alergenos>? = null,
    var descripcion : List<Descripcion>? = null,
    var ingredientes : List<Producto>? = null
): Serializable{
    companion object{
        val RECETAS_BASE = "recetas"
        val RECETAS_USUARIOS = "recetasUsuarios"
    }
}