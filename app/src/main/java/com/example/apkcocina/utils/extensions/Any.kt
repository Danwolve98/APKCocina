package com.example.apkcocina.utils.extensions

import android.content.Context
import androidx.compose.ui.platform.debugInspectorInfo
import com.example.apkcocina.APKCocinaApplication

fun Any?.notNull(action : () -> Unit) {
    if(this != null)
        action()
}


fun Any?.notNull(notNullAction : () -> Unit,nullAction : () -> Unit){
    if(this != null)
        notNullAction()
    else
        nullAction()
}

fun<T:Any?> T?.notNullorDefault(defaultValue : T) : T = this ?: defaultValue

fun<T:Any?> T?.notNullorDefault(notNullAction : () -> T,defaultValue : T) : T{
    return if(this != null)
                notNullAction()
            else
                defaultValue
}

fun<T:Any?> T.notNullorDefault(notNullAction : () -> T,nullAction : () -> T) : T{
   if(this != null){
        notNullAction()
    }
    else
        nullAction()
    return this
}

fun Any.getContext() : Context = APKCocinaApplication().applicationContext