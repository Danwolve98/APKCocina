package com.example.apkcocina.utils.model

import com.google.firebase.firestore.PropertyName
import java.util.Calendar
import java.util.Date

class User(
    @PropertyName("id")
    var id : String? = "",
    @PropertyName("nombre")
    var nombre : String? = null,
    @PropertyName("apellidos")
    var apellidos : String? = null,
    @PropertyName("recetas")
    var recetas : List<Int>? = null,
    @PropertyName("nacionalidad")
    var nacionalidad : String? = "Española",
    @PropertyName("cumpleanos")
    var cumpleanos : Calendar? = null,
    @PropertyName("fecha_de_registro")
    var fechaDeRegistro : Calendar? = null){

    companion object{
        const val USUARIOS = "users"
    }

/*    private fun contrasenaChanged(newPassword : String) : Pair<String,ByteArray> {
        val salt = ByteArray(16)
        val random = SecureRandom()
        random.nextBytes(salt)

        return hashPassword(newPassword, salt)
    }

    fun hashPassword(password: String, salt: ByteArray): Pair<String,ByteArray> {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray(Charsets.UTF_8))
        return Pair(hashedPassword.joinToString("") { "%02x".format(it) },salt)
    }*/


}