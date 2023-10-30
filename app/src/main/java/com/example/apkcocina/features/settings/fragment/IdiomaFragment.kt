package com.example.apkcocina.features.settings.fragment

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgIdiomaBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.android.material.button.MaterialButton
import java.util.Locale

class IdiomaFragment : BaseFragment<FrgIdiomaBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private lateinit var lanPair : Pair<List<String>,List<MaterialButton>>
    private lateinit var bt : MaterialButton

    override fun onAttach(context: Context) {
        actionBar = TitleActionBar(getString(R.string.idioma))
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalizeView()
    }

    private fun initalizeView() {
            val language : String =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireContext().getSystemService(LocaleManager::class.java).applicationLocales[0]?.language ?: Constants.LANGUAGE_ES
                }else{
                    AppCompatDelegate.getApplicationLocales()[0]?.language ?: Constants.LANGUAGE_ES
                }
            with(binding){
                bt = when(language){
                    Constants.LANGUAGE_ES-> btEspanol
                    Constants.LANGUAGE_EN-> btIngles
                    Constants.LANGUAGE_FR-> btFrancais
                    else -> {btEspanol}
                }
                lanPair = Pair(listOf(Constants.LANGUAGE_ES,Constants.LANGUAGE_FR,Constants.LANGUAGE_EN),
                    listOf(btEspanol,btFrancais,btIngles))
            }

            assignLanguageButtons()

            bt.apply{
                isChecked = true
                isClickable = false
            }
    }

    private fun assignLanguageButtons() {
        for ((index, b) in lanPair.second.withIndex()) {
            b.setOnClickListener {
                changeIdioma(lanPair.first[index])
                it.isClickable = false
                lanPair.second
                    .filter { materialButton ->
                        materialButton.id != b.id
                    }
                    .forEach { materialButton ->
                        materialButton.apply {
                            isChecked = false
                            isClickable = true
                        }
                    }
            }
        }
    }

    /**
     * Función que se encarga de cambiar el idioma de la aplicación en base a la versión del dispositivo
     * @param idioma el idioma a cambiar
     */
    private fun changeIdioma(idioma : String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(
                Locale.forLanguageTag(idioma))
        }else{
            val appLocale : LocaleListCompat = LocaleListCompat.forLanguageTags(idioma)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

}