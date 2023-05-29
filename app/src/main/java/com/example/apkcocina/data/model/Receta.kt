package com.example.apkcocina.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apkcocina.utils.model.Alergenos
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
): Serializable