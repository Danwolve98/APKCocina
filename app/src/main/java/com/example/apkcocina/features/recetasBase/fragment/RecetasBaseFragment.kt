package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apkcocina.R
import com.example.apkcocina.data.model.Receta
import com.example.apkcocina.databinding.RecetasBaseFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasBase.viewModel.RecetasBaseViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Alergenos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetasBaseFragment() : BaseFragment() {

    val recetasBaseViewModel : RecetasBaseViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : RecetasBaseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.recetas_base))
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recetasBaseViewModel.loadRecetas()
        recetasBaseViewModel.loading.observe(this, Observer {
            mainActivity.setLoading(it)
        })
        recetasBaseViewModel.mutableRecetas.observe(this, Observer {
            binding.rvRecetasBase.adapter = RecetasAdapter(it,{onRecetaClickListener(it)})
        })
        /*mainActivity.setLoading(true)
        dbFirestore.collection("recetas").get(Source.DEFAULT)
            .addOnSuccessListener {result->
                result.documentChanges
                for(r in result){
                    val receta = r.toObject<Receta>()
                    if(!recetas.contains(receta)){
                        recetas.add(receta)
                    }
                }
                mainActivity.setLoading(false)
            }
            .addOnFailureListener {
                mainActivity.setLoading(false)
            }*/
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
        /*loadRecetas()*/
    }

    /*//TODO CAMBIAR METODO PARA AGREGAR RECETAS SIN VERIFICAR TODAS
    private fun loadRecetas(){
        binding.rvRecetasBase.adapter = RecetasAdapter(recetas,{onRecetaClickListener(it)})
    }*/

    private fun onRecetaClickListener(receta: Receta){ mainActivity.navigate(R.id.action_recetasBaseFragment_to_recetaDetalle,Bundle().apply { putSerializable("receta",receta) }) }

}