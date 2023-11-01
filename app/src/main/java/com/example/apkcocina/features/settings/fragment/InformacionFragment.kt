package com.example.apkcocina.features.settings.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.ImageGetter
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgInformacionBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException


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
        binding.webView.setBackgroundColor(Color.TRANSPARENT)
        binding.webView.loadData(
            when(navArgs.info){
                1-> getString(R.string.ayuda_general)
                2-> getString(R.string.info_general)
                else -> {""}
            },
            "text/html",
            "UTF-8"
        )
    }
}