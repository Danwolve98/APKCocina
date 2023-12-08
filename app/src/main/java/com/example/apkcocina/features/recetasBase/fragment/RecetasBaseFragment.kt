package com.example.apkcocina.features.recetasBase.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasBaseBinding
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasBase.viewModel.RecetasBaseViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.collectFlow
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetasBaseFragment() : BaseFragment<FrgRecetasBaseBinding>() {

    private val recetasBaseViewModel : RecetasBaseViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.recetas_base))
    }

    override fun initializeView() {
        super.initializeView()
        recetasBaseViewModel.loadRecetas()
    }

    override fun initializeObservers() {
        super.initializeObservers()
        collectFlow(recetasBaseViewModel.loading){
            mainActivity.setLoading(it.isLoading)
        }

        recetasBaseViewModel.recetas.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let {
                binding.rvRecetas.adapter = RecetasAdapter(it) {receta->
                    onRecetaClickListener(
                        receta
                    )
                }
            }
        }

        recetasBaseViewModel.recetasError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }

    }

    private fun onRecetaClickListener(receta: Receta){ navigate(NavGraphDirections.actionGlobalRecetaDetalle(receta.nombre ?: getString(R.string.unkown),receta.id,Receta.RECETAS_BASE)) }

}