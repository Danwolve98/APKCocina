package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.apkcocina.data.model.Receta
import com.example.apkcocina.databinding.RecetaDetalleFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar

class RecetaDetalle : BaseFragment() {

    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : RecetaDetalleFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var receta : Receta

    override fun onAttach(context: Context) {
        super.onAttach(context)
        receta = arguments?.getSerializable("receta") as Receta
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(receta.nombre.toString())
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecetaDetalleFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        Glide.with(requireContext()).load(receta.imagenes?.get(0)).into(binding.ivDetalle)



    }


}