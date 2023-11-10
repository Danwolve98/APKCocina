package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Build
import com.bumptech.glide.Glide
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRecetaDetalleBinding
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetaDetalle : BaseFragment<FrgRecetaDetalleBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private lateinit var receta : Receta
    @Inject
    lateinit var firebaseStorage : FirebaseStorage

    override fun onAttach(context: Context) {
        receta = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("receta",Receta::class.java) ?: Receta()
        }else{
            arguments?.getSerializable("receta") as Receta
        }

        super.onAttach(context)
    }

   /* int spanCount = 3; // 3 columns
    int spacing = 50; // 50px
    boolean includeEdge = true;
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/

    override fun assingActionBar() {
        actionBar = TitleActionBar(receta.nombre.toString())
    }

    override fun initializeView() {
        cargarDatos()
    }

    private fun cargarDatos(){
        /*val storageReference = firebaseStorage.getReference(receta.getReferencia())
        storageReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it.toString()).into(binding.ivDetalle)
        }*/
        binding.apply {
            tvNombreReceta.text = receta.nombre
            tvTiempoReceta.text = formatearTiempo(receta.tiempoPreparacion)
            /*tvDescripcion.text = receta.descripcion ?: getString(R.string.sin_descripcion)*/
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