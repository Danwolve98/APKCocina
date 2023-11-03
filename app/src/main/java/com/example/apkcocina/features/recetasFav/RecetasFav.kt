package com.example.apkcocina.features.recetasFav

import android.content.Context
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasFavBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar

class RecetasFav : BaseFragment<FrgRecetasFavBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.favoritos)).apply { haveBack = false }
    }
}