package com.example.apkcocina.features.settings.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgInformacionBinding
import com.example.apkcocina.MainActivity
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformacionFragment : BaseFragment<FrgInformacionBinding>() {

     override lateinit var actionBar: APKCocinaActionBar
     private val navArgs : InformacionFragmentArgs by navArgs()

     override fun onAttach(context: Context) {
         actionBar = TitleActionBar(getString(R.string.informacion))
         super.onAttach(context)
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvInfoAjustes.text =
            when(navArgs.info){
                1-> "PATATA"
                2-> "POMELO"
                else->""
        }
    }

}