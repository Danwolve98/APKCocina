package com.example.apkcocina.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionScene.Transition
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import arrow.core.const
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apkcocina.R


fun ImageView.loadImage(uri : Uri?) {

    val progressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 25f
        setColorSchemeColors(
            getColorVersionCheck(R.color.base_1,context),
            getColorVersionCheck(R.color.base_2,context),
            getColorVersionCheck(R.color.base_3,context),
            getColorVersionCheck(R.color.base_4,context))
        start()
    }

    Glide.with(this)
        .asDrawable()
        .load(uri)
        .error(R.drawable.ic_chef)
        .placeholder(progressDrawable)
        .addListener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                Toast.makeText(context, context.getString(R.string.error_al_cargar_la_foto), Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)
}

fun ImageView.loadImage(string : String?) {

    val progressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 25f
        setColorSchemeColors(
            getColorVersionCheck(R.color.base_1,context),
            getColorVersionCheck(R.color.base_2,context),
            getColorVersionCheck(R.color.base_3,context),
            getColorVersionCheck(R.color.base_4,context))
        start()
    }

    Glide.with(this)
        .asDrawable()
        .load(string)
        .centerCrop()
        .error(R.drawable.apkcocina_logo)
        .placeholder(progressDrawable)
        .addListener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)
}

fun ImageView.loadImage(uri : ByteArray?) {

    val progressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 25f
        setColorSchemeColors(
            getColorVersionCheck(R.color.base_1,context),
            getColorVersionCheck(R.color.base_2,context),
            getColorVersionCheck(R.color.base_3,context),
            getColorVersionCheck(R.color.base_4,context))
        start()
    }

    Glide.with(this)
        .asDrawable()
        .load(uri)
        .error(R.drawable.ic_chef)
        .placeholder(progressDrawable)
        .addListener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                Toast.makeText(context, context.getString(R.string.error_al_cargar_la_foto), Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)
}


private fun getColorVersionCheck(colorId: Int,context: Context) : Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(colorId)
    else
        context.resources.getColor(colorId)