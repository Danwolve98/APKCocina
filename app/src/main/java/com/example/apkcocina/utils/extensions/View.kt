package com.example.apkcocina.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.hideKeyboard(completed: () -> Unit = {}) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val wasOpened = inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    if (!wasOpened) completed()
}
