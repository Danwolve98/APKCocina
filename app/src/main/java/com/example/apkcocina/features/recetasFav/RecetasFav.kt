package com.example.apkcocina.features.recetasFav

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasBaseBinding
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasOnline.viewModel.GetRecetasViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Receta
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecetasFav : BaseFragment<FrgRecetasBaseBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    val viewModel : GetRecetasViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.favoritos)).apply { haveBack = false }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getRecetasFav()
    }

    override fun initializeObservers() {
        super.initializeObservers()
        viewModel.getRecetasFav.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {listaRecetas->
                binding.rvRecetas.adapter = RecetasAdapter(listaRecetas) { receta ->
                    onRecetasClick(receta)
                }
            }
        }

        viewModel.getRecetasFavError.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onRecetasClick(receta: Receta){
        navigate(NavGraphDirections.actionGlobalRecetaDetalle(receta.nombre ?: getString(R.string.unkown),receta.id))
    }
}