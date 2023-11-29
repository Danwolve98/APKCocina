package com.example.apkcocina.utils.extensions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.bumptech.glide.Glide
import com.example.apkcocina.utils.base.Constants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


fun Uri.toBase64(context : Context) : String?{
    val contentResolver: ContentResolver = context.contentResolver

    try {
        // Abre un InputStream desde la Uri
        val inputStream: InputStream? = contentResolver.openInputStream(this)

        // Convierte el InputStream a ByteArray
        val byteArray = inputStream?.readBytes()

        inputStream?.close()
        // Convierte el ByteArray a Base64
        return if (byteArray != null) {
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun String.base64toByteArray() : ByteArray = Base64.decode(this,Base64.DEFAULT)