package com.example.apkcocina.utils.extensions

import android.content.Context
import androidx.compose.ui.platform.debugInspectorInfo
import com.example.apkcocina.APKCocinaApplication

/**
 * Ejecuta una función si no es nulo
 */
fun Any?.notNull(action : () -> Unit) {
    if(this != null)
        action()
}

/**
 * Ejecuta una función si no es uno u otra si, sí lo és
 * @param notNullAction la acción si NO es nulo
 * @param nullAction la acción si ES nulo
 */
fun Any?.notNull(notNullAction : () -> Unit,nullAction : () -> Unit){
    if(this != null)
        notNullAction()
    else
        nullAction()
}

/**
 * Función que devuelve el valor si no es nulo o el valor por especificado si, sí que lo és
 * @param defaultValue el valor especificado
 * @return [T]
 */
fun<T:Any?> T?.notNullorDefault(defaultValue : T) : T = this ?: defaultValue

/**
 * Funciñon que ejecuta una función si el valor no es nulo o devuelve un valor por defecto si, sí lo és
 * @param notNullAction la acción si no es nulo
 * @param defaultValue el valor a retornar si sí que és nulo
 * @return [T]
 */
fun<T:Any?> T?.notNullorDefault(notNullAction : () -> T,defaultValue : T) : T{
    return if(this != null)
                notNullAction()
            else
                defaultValue
}

/**
 * Función que ejecuta una función en caso de no ser nulo y o otra si si que lo es, siempre devuelve el tipo sobre el que se llama
 * @param notNullAction la acción si no es nulo
 * @param nullAction la accion en caso de ser nulo
 * @return [T]
 */
fun<T:Any?> T.notNullorDefault(notNullAction : () -> T,nullAction : () -> T) : T{
   if(this != null){
        notNullAction()
    }
    else
        nullAction()
    return this
}

/**
 * Devuelve el contexto de la aplicación
 */
@Deprecated("Función obseleta, ahora el contexto esta injectado por [Hilt] en el modulo de la aplicación",
    ReplaceWith("@Inject constructor(val context : Context)", ""),
    DeprecationLevel.WARNING
)
fun Any.getContext() : Context = APKCocinaApplication().applicationContext