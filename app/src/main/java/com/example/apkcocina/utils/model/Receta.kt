package com.example.apkcocina.utils.model

class Receta(
    var nombre : String,
    var tiempoPreparacion : Int,
    var alergenos: List<Alergenos>,
    var descripcion : String,
    var imagenes : List<String>
) {

}