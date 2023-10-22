package com.example.apkcocina.utils.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import kotlin.String


    fun String.removeAccents(): String {
        val regex = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        return regex.replace(temp, "")
    }

    fun String.fromHtml() : Spanned{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(this)
        }
    }

