package com.example.apkcocina.features.profile.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRegisterBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar

class RegisterFragment : BaseFragment<FrgRegisterBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    override fun onAttach(context: Context) {
        actionBar = TitleActionBar(getString(R.string.register))
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        with(binding){
            btRegistrarse.setOnClickListener{}
        }
    }
}