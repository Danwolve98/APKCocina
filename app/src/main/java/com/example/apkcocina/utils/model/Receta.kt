package com.example.apkcocina.utils.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.errorprone.annotations.Keep
import java.io.Serializable
@Keep
@Entity(tableName = "recetas")
class Receta(
    @PrimaryKey(autoGenerate = false)
    var id : String = "",
    var usuario : String? = null,
    var nombre : String? = null,
    var imagenPrincipal : String? = null,
    var tiempoPreparacion : Int? = null,
    var alergenos: List<Alergenos>? = null,
    var descripcion : List<Descripcion>? = null,
    var ingredientes : List<Producto>? = null,
    val usuariosPuntuacion : List<String> = listOf(),
    var puntuacion : Float = 0f
): Serializable{
    companion object{
        val RECETAS_BASE = "recetas"
        val RECETAS_USUARIOS = "recetasUsuarios"
    }
}