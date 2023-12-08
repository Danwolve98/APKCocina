package com.example.apkcocina.features.recetasOnline.fragment

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
class RecetasOnlineFragment : BaseFragment<FrgRecetasBaseBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    val viewModel : GetRecetasViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.recetas_online))
    }

    override fun initializeView() {
        super.initializeView()
        viewModel.getRecetas()
    }
    override fun initializeObservers() {
        super.initializeObservers()
        viewModel.recetasResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {listaRecetas->
                binding.rvRecetas.adapter = RecetasAdapter(listaRecetas) { receta ->
                    onRecetasClick(receta)
                }
            }
        }

        viewModel.recetasError.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), getString(R.string.error_en_el_servidor), Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    private fun onRecetasClick(receta: Receta){
        navigate(NavGraphDirections.actionGlobalRecetaDetalle(receta.nombre ?: getString(R.string.unkown),receta.id,Receta.RECETAS_USUARIOS))
    }

}