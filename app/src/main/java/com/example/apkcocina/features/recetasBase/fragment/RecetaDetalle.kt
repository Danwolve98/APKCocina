package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetaDetalleBinding
import com.example.apkcocina.features.crearReceta.adapter.AlergenosAdapter
import com.example.apkcocina.features.recetasBase.adapter.IngredientesAdapter
import com.example.apkcocina.features.recetasOnline.adapter.RecetaDetalleDescripcionAdapter
import com.example.apkcocina.features.recetasOnline.viewModel.GetRecetasViewModel
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.InfoActionBar
import com.example.apkcocina.utils.dialog.InfoRecetasDialog
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.loadImage
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.playAnimation
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecetaDetalle : BaseFragment<FrgRecetaDetalleBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private val args : RecetaDetalleArgs by navArgs()
    private val viewModel: GetRecetasViewModel by viewModels()
    private lateinit var receta : Receta
    private var nuevaPuntuacion : Float = 0f
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(args.idReceta != null)
            viewModel.getReceta(args.idReceta,args.collection)
        else {
            Toast.makeText(requireContext(), getString(R.string.error_en_el_servidor), Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    override fun assingActionBar() {
        actionBar = InfoActionBar(args.name,::mostrarDialogRecetas)
    }

    private fun mostrarDialogRecetas() = InfoRecetasDialog(requireContext(),false).show()

    override fun initializeView() {
        super.initializeView()
        if(args.collection == Receta.RECETAS_BASE){
            with(binding){
                ratingBarReceta.invisible()
                clUsuario.invisible()
                tgFavReceta.invisible()
            }
        }

        binding.ratingBarReceta.setOnRatingBarChangeListener { ratingBar: RatingBar, fl: Float, userInteract: Boolean ->
            if(userInteract){
                nuevaPuntuacion = (receta.puntuacion + fl)/2
                ratingBar.rating = nuevaPuntuacion
                viewModel.updatePuntuacionReceta(receta.id,nuevaPuntuacion)
            }
        }
    }

    override fun initializeObservers() {
        super.initializeObservers()
        collectFlow(viewModel.recetasFavState){
            mainActivity.setLoading(it.isLoading)
        }
        viewModel.recetaResult.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let{pair->
                cargarDatos(pair.first,pair.second)
            }
        }

        viewModel.recetaError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let{error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }

        viewModel.setRecetaFav.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let{_->
                Toast.makeText(requireContext(), getString(R.string.receta_guardada_en_favoritos), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.setRecetaFavError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.removeRecetaFav.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { _->
                Toast.makeText(requireContext(), getString(R.string.receta_borrada_de_favoritos), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.removeRecetaFavError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.recetaPuntuacion.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { _->
                Toast.makeText(requireContext(), "Puntuación actualizada", Toast.LENGTH_SHORT).show()
                receta.puntuacion = nuevaPuntuacion
            }
        }

        viewModel.recetaError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                binding.ratingBarReceta.rating = receta.puntuacion
            }
        }

        viewModel.recetaPuntuacionYaActualizada.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { _->
                Toast.makeText(requireContext(), "Ya habías puntuado esta receta", Toast.LENGTH_SHORT).show()
                binding.ratingBarReceta.rating = receta.puntuacion
            }
        }
    }

    private fun cargarDatos(receta : Receta,isFav : Boolean){
        this.receta = receta
        binding.apply {
            tvNombreReceta.text = receta.nombre
            tvTiempoReceta.text = formatearTiempo(receta.tiempoPreparacion)
            receta.alergenos.notNull { rvAlergenos.adapter = AlergenosAdapter(it,false) }

            tgFavReceta.isChecked = isFav
            tgFavReceta.addOnCheckedChangeListener { bt, isChecked ->
                bt.playAnimation(R.anim.click_animation)
                viewModel.changeFav(receta.id,isChecked)
            }

            ivReceta.loadImage(receta.imagenPrincipal)

            rvAlergenos.layoutManager = object  : FlexboxLayoutManager(requireContext(),FlexDirection.ROW,FlexWrap.WRAP){
                init {
                    justifyContent = JustifyContent.FLEX_START
                    alignItems = AlignItems.FLEX_START
                }

                override fun getFlexItemAt(index: Int): View {
                    val item = super.getFlexItemAt(index)

                    val params = item.layoutParams as LayoutParams
                    params.flexBasisPercent = 0.15f
                    params.isWrapBefore = index > 0 && index % 6 == 0

                    return item
                }

            }

            binding.ratingBarReceta.rating = receta.puntuacion

            receta.ingredientes.notNull {ingredientes-> rvIngredientes.adapter = IngredientesAdapter(ingredientes.map {producto-> producto.toString() }.toMutableList(),requireContext()) }

            receta.descripcion.notNull { rvInstrucciones.adapter = RecetaDetalleDescripcionAdapter(it) }
        }
    }

    private fun formatearTiempo(tiempo:Int?) : String =
        if(tiempo == null)
            "?"
        else{
            val hours = tiempo / 60
            val minutosRestantes = tiempo % 60

            "%dh/%dmins".format(hours,minutosRestantes)
        }

}