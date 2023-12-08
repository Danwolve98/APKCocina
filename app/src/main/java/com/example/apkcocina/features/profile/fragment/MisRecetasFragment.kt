package com.example.apkcocina.features.profile.fragment

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetasBaseBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasOnline.viewModel.GetRecetasViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Receta
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MisRecetasFragment : BaseFragment<FrgRecetasBaseBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    val viewModel : ProfileViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.mis_recetas))
    }

    override fun initializeView() {
        super.initializeView()
        viewModel.cargarRecetasUsuario()
    }
    override fun initializeObservers() {
        super.initializeObservers()
        viewModel.recetasUsuario.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {listaRecetas->
                binding.rvRecetas.adapter = RecetasAdapter(listaRecetas) { receta ->
                    onRecetasClick(receta)
                }
            }
        }

        viewModel.recetasUsuarioFail.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), getString(R.string.error_en_el_servidor), Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    private fun onRecetasClick(receta: Receta){
        navigate(NavGraphDirections.actionGlobalRecetaDetalle(receta.nombre ?: getString(R.string.unkown),receta.id))
        Toast.makeText(requireContext(), "CLICK ${receta.nombre}", Toast.LENGTH_SHORT).show()
    }

}