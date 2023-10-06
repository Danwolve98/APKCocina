package com.example.apkcocina.features.settings.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgAjustesBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar

class SettingsFragment : BaseFragment<FrgAjustesBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionBar = TitleActionBar(getString(R.string.ajustes)).apply { haveBack=false }
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {

    }


}