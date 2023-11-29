package com.example.apkcocina.utils.model

import com.example.apkcocina.R


enum class Alergenos(val drawableId : Int,var checked: Boolean = false){
        GLUTEN(R.drawable.al_gluten_selector),
        CRUSTACEOS(R.drawable.al_crustaceos_selector),
        HUEVOS(R.drawable.al_huevo_selector),
        PESCADO(R.drawable.al_pescado_selector),
        CACAHUETES(R.drawable.al_cacahuetes_selector),
        SOJA(R.drawable.al_soja_selector),
        LACTEOS(R.drawable.al_lacteos_selector),
        FRUTOSDECASCARA(R.drawable.al_frutos_de_cascara_selector),
        APIO(R.drawable.al_apio_selector),
        MOSTAZA(R.drawable.al_mostaza_selector),
        GRANOSSESAMO(R.drawable.al_granos_de_sesamo_selector),
        MOLUSCO(R.drawable.al_moluscos_selector),
        ALTRAMUCES(R.drawable.al_altramuces_selector),
        SULFITOS(R.drawable.al_dioxido_de_azufre_y_sulfitos_selector)
}
