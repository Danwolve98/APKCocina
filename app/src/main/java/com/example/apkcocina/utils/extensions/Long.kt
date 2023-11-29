package com.example.apkcocina.utils.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.timeToCalendar() : Calendar = Calendar.getInstance().apply { timeInMillis = this@timeToCalendar }

fun Calendar.format(format : String) : String{
    val simpleFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleFormat.format(this.time)
}
