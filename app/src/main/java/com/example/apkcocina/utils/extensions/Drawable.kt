package com.example.apkcocina.utils.extensions

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.apkcocina.utils.base.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

fun Drawable.getUri(): Uri? {
    // Convierte el Drawable en un Bitmap
    val bitmap = when (this) {
        is BitmapDrawable -> this.bitmap
        else -> {
            // Si el Drawable no es un BitmapDrawable, crea un nuevo Bitmap y lo dibuja
            val width = intrinsicWidth
            val height = intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
            bitmap
        }
    }

    // Guarda el Bitmap en un archivo temporal
    try {
        val cachePath = File(this.getContext().cacheDir, Constants.PROFILE_IMAGES_CACHE)
        cachePath.mkdirs()
        val stream: OutputStream = FileOutputStream(cachePath.absolutePath + "/temp_image.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    // Obtiene la Uri del archivo temporal
    val imagePath = File(this.getContext().cacheDir, Constants.PROFILE_IMAGES_CACHE)
    val newFile = File(imagePath, "temp_image.png")
    return Uri.fromFile(newFile)
}