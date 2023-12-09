package com.example.apkcocina.utils.model

import java.io.Serializable

class Producto : Serializable {
    var nombre : String? = null
    var cantidad : Float? = null
    var tipo : String? = null

    fun changeTipo(tipo : String) {this.tipo = tipo}

    override fun toString(): String = "· ${nombre ?: ""} \t${cantidad?.toInt() ?: ""} ${tipo ?: ""}"

}