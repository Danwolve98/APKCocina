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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.stream.Stream


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

fun Uri.toInputStream(context : Context) : InputStream?{
    val contentResolver: ContentResolver = context.contentResolver
    return try {
        contentResolver.openInputStream(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Uri.toByteArray(context : Context) : ByteArray?{
    val contentResolver: ContentResolver = context.contentResolver
    return try {
        // Abre un InputStream desde la Uri
        val inputStream: InputStream? = contentResolver.openInputStream(this)

        // Convierte el InputStream a ByteArray
        val byteArray = inputStream?.readBytes()

        inputStream?.close()
        // Convierte el ByteArray a Base64
        byteArray
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String.base64toByteArray() : ByteArray = Base64.decode(this,Base64.DEFAULT)

fun String.base64ToUri(context: Context): Uri? {
    try {
        val decodedBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
        val tempFile = File.createTempFile("tempImage", null, context.cacheDir)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(decodedBytes)
        fileOutputStream.close()
        return Uri.fromFile(tempFile)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}