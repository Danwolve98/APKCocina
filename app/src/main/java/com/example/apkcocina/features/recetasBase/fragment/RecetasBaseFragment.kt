package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasBaseBinding
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.home.activity.MainActivity
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

    private lateinit var mainActivity: MainActivity
    private var listaRecetasBase : List<Receta>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.recetas_base))
        (activity as MainActivity).configureActionBar(this)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        (activity as MainActivity).configureActionBar(this)
    }

    private fun onRecetaClickListener(receta: Receta){ mainActivity.navigate(R.id.action_recetasBaseFragment_to_recetaDetalle,Bundle().apply { putSerializable("receta",receta) }) }

}