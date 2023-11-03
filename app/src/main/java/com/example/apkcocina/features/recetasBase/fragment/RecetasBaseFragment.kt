package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasBaseBinding
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.MainActivity
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasBase.viewModel.RecetasBaseViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetasBaseFragment() : BaseFragment<FrgRecetasBaseBinding>() {

    @Inject
    lateinit var firebaseStorage: FirebaseStorage
    val recetasBaseViewModel : RecetasBaseViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar

    private var listaRecetasBase : List<Receta>? = null

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.recetas_base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recetasBaseViewModel.loadRecetas()
        recetasBaseViewModel.loading.observe(this, Observer {
            mainActivity.setLoading(it)
        })
        recetasBaseViewModel.mutableRecetas.observe(this, Observer {
           listaRecetasBase = it
            binding.rvRecetasBase.adapter = RecetasAdapter(listaRecetasBase!!,{onRecetaClickListener(it)},firebaseStorage)
        })
    }

    private fun onRecetaClickListener(receta: Receta){ mainActivity.navigate(R.id.action_recetasBaseFragment_to_recetaDetalle,Bundle().apply { putSerializable("receta",receta) }) }

}