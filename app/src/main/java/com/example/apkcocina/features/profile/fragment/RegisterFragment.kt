package com.example.apkcocina.features.profile.fragment

import android.content.Context
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRegisterBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar

class RegisterFragment : BaseFragment<FrgRegisterBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionBar = TitleActionBar(getString(R.string.register))
    }
}