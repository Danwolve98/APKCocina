package com.example.apkcocina.data.model
import com.example.apkcocina.utils.model.Alergenos
import java.io.Serializable
class Receta(
    var nombre : String? = null,
    var tiempoPreparacion : Int? = null,
    var alergenos: List<Alergenos>? = null,
    var descripcion : String? = null,
    var imagenes : List<String>? = null,
    var ingredientes : HashMap<String,String>? = null
): Serializable