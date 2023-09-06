package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.databinding.RecetaDetalleFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetaDetalle : BaseFragment<RecetaDetalleFragmentBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    private lateinit var mainActivity: MainActivity
    private lateinit var receta : Receta
    @Inject
    lateinit var firebaseStorage : FirebaseStorage

    override fun onAttach(context: Context) {
        super.onAttach(context)
        receta = arguments?.getSerializable("receta") as Receta
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(receta.nombre.toString())
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        cargarDatos()
    }

    private fun cargarDatos(){
        val storageReference = firebaseStorage.getReference(receta.getReferencia())
        storageReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it.toString()).into(binding.ivDetalle)
        }
        binding.apply {
            tvNombreReceta.text = receta.nombre
            tvTiempoEstimado.text = formatearTiempo(receta.tiempoPreparacion)
            tvDescripcion.text = receta.descripcion ?: getString(R.string.sin_descripcion)
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