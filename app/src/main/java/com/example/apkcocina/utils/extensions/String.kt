package com.example.apkcocina.utils.extensions

import kotlin.String


    fun String.removeAccents(): String {
        val regex = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        return regex.replace(temp, "")
    }

