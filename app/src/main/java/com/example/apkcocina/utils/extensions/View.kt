package com.example.apkcocina.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.Image
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.GONE
}

fun View.visibilityCheck(boolean: Boolean) = if(boolean) this.visible() else this.invisible()

fun View.hideKeyboard(){
    this.clearFocus()
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.dismissKeyboard(completed: () -> Unit = {}) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val wasOpened = inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    if (!wasOpened) completed()
}

fun View.playAnimation(animId : Int) = startAnimation(AnimationUtils.loadAnimation(context,animId))

fun ImageView.applyBrightness(brightness: Float) {
    val cm = ColorMatrix().apply { set(floatArrayOf(
        brightness, 0f, 0f, 0f, 0f,
        0f, brightness, 0f, 0f, 0f,
        0f, 0f, brightness, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    ))}
    this.colorFilter = ColorMatrixColorFilter(cm)
}

fun ImageView.applySaturation(saturation: Float) {
    val cm = ColorMatrix().apply { setSaturation(saturation) }
    this.colorFilter = ColorMatrixColorFilter(cm)
}



