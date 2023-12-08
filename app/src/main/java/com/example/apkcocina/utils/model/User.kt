package com.example.apkcocina.utils.model

import androidx.room.Delete
import com.google.firebase.firestore.PropertyName
import java.util.Calendar

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
    @PropertyName("foto")
    var foto : String? = null,
    @PropertyName("cumpleanos")
    var cumpleanos : Long? = null,
    @PropertyName("fecha_de_registro")
    var fechaDeRegistro : Long? = null,
    @PropertyName("recetasFav")
    var recetasFav : List<String>? = null){

    companion object{
        const val USUARIOS = "users"
    }

    fun cumpleanosToCalendar() : Calendar? =
        if(cumpleanos == null)
            null
        else
            Calendar.getInstance().apply {
                timeInMillis = cumpleanos as Long
            }

    fun fechaDeRegistroToCalendar() : Calendar? =
        if(fechaDeRegistro == null)
            null
        else
            Calendar.getInstance().apply {
                timeInMillis = fechaDeRegistro as Long
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