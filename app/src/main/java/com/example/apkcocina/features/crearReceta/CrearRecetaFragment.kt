package com.example.apkcocina.features.crearReceta

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.apkcocina.R
import com.example.apkcocina.data.model.Receta
import com.example.apkcocina.databinding.CrearRecetaFragmentBinding
import com.example.apkcocina.databinding.RecetaDetalleFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Alergenos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CrearRecetaFragment : BaseFragment(){

    @Inject
    lateinit var firebaseStorage: FirebaseStorage
    @Inject
    lateinit var firestore: FirebaseFirestore
    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : CrearRecetaFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var receta : Receta

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.crear_receta))
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CrearRecetaFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        (activity as MainActivity).configureActionBar(this)

        binding.btCrearReceta.setOnClickListener {
            val receta = Receta().apply {
                descripcion = "COMIDA RICA EN POTASIO"
                nombre = "POTATSIO PLATANOSO"
                alergenos = listOf(Alergenos.ALTRAMUCES,Alergenos.APIO,Alergenos.GLUTEN)
                ingredientes = hashMapOf<String,String>().apply { put("papata","2")
                    put("potatsio","1g")}
                tiempoPreparacion = 70
                imagenes = listOf()
            }

            val collection = firestore.collection(Receta.RECETAS_USUARIOS)

            collection.add(receta).addOnSuccessListener {documentReference->
                Log.d("TAG", "Documento agregado con ID: ${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.e("TAG", "Error al agregar el documento", e)
                }
        }



    }

}