package com.example.apkcocina.utils.model

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Calendar
import java.util.Date

data class User(
    var nombre : String? = null,
    var apellidos : String? = null,
    var recetas : List<Receta>? = null,
    var nacionalidad : String = "Española",
    var cumpleanos : Date = Calendar.getInstance().time){
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