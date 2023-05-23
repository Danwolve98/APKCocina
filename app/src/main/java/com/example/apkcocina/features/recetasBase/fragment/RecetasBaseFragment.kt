package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apkcocina.R
import com.example.apkcocina.data.model.Receta
import com.example.apkcocina.databinding.RecetasBaseFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Alergenos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class RecetasBaseFragment() : BaseFragment() {

    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : RecetasBaseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private val dbFirestore by lazy { FirebaseFirestore.getInstance() }
    var recetas : MutableList<Receta> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.recetas_base))
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RecetasBaseFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        loadRecetas()


    }

    //TODO CAMBIAR METODO PARA AGREGAR RECETAS SIN VERIFICAR TODAS
    private fun loadRecetas(){
        mainActivity.setLoading(true)
        dbFirestore.collection("recetas").get(Source.DEFAULT)
            .addOnSuccessListener {result->
                result.documentChanges
                for(r in result){
                    val receta = r.toObject<Receta>()
                    if(!recetas.contains(receta)){
                        recetas.add(receta)
                    }
                }
                binding.rvRecetasBase.adapter = RecetasAdapter(recetas,{onRecetaClickListener(it)})
                mainActivity.setLoading(false)
            }
            .addOnFailureListener {
                mainActivity.setLoading(false)
            }
    }

    private fun onRecetaClickListener(receta: Receta){ mainActivity.navigate(R.id.action_recetasBaseFragment_to_recetaDetalle,Bundle().apply { putSerializable("receta",receta) }) }

}