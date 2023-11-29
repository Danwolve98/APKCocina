package com.example.apkcocina.utils.extensions

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_chef)
        .placeholder(progressDrawable)
        .into(this)
}

fun ImageView.loadImage(uri : String?) {

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
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_chef)
        .placeholder(progressDrawable)
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
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_chef)
        .placeholder(progressDrawable)
        .into(this)
}


private fun getColorVersionCheck(colorId: Int,context: Context) : Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(colorId)
    else
        context.resources.getColor(colorId)