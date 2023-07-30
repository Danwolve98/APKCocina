package com.example.apkcocina.utils.model

import java.io.Serializable

class Producto : Serializable {
    var nombre : String? = null
    var cantidad : Float? = null
    val tipo : Tipos? = null
}