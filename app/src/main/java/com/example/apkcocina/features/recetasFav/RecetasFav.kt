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
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.visible
import com.example.apkcocina.utils.model.Receta
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecetasFav : BaseFragment<FrgRecetasBaseBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    val viewModel : GetRecetasViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.favoritos)).apply { haveBack = false }
    }

    override fun initializeView() {
        super.initializeView()
        viewModel.getRecetasFav()
    }

    override fun initializeObservers() {
        super.initializeObservers()
        collectFlow(viewModel.recetasFavState){
            mainActivity.setLoading(it.isLoading)
        }

        viewModel.getRecetasFav.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {listaRecetas->
                binding.rvRecetas.adapter = RecetasAdapter(listaRecetas) { receta ->
                    onRecetasClick(receta)
                }
            }
        }

        viewModel.getRecetasFavError.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {error->
                with(binding){
                    tvSinRecetasFav.apply {
                        text = getString(R.string.debes_iniciar_sesion)
                        visible()
                    }
                    animationCat.apply {
                        setAnimation(R.raw.cookies_animation)
                        visible()
                    }
                }
            }
        }

        viewModel.sinRecetasFav.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                with(binding){
                    binding.tvSinRecetasFav.visible()
                    binding.animationCat.visible()
                }

            }
        }
    }

    private fun onRecetasClick(receta: Receta){
        navigate(NavGraphDirections.actionGlobalRecetaDetalle(receta.nombre ?: getString(R.string.unkown),receta.id,Receta.RECETAS_USUARIOS))
    }
}