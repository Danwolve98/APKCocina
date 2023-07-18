package com.example.apkcocina.features.recetasBase.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.databinding.RecetasBaseFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.recetasBase.adapter.RecetasAdapter
import com.example.apkcocina.features.recetasBase.viewModel.RecetasBaseViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecetasBaseFragment() : BaseFragment() {

    @Inject
    lateinit var firebaseStorage: FirebaseStorage
    val recetasBaseViewModel : RecetasBaseViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : RecetasBaseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private var listaRecetasBase : List<Receta>? = null

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
           listaRecetasBase = it
            binding.rvRecetasBase.adapter = RecetasAdapter(listaRecetasBase!!,{onRecetaClickListener(it)},firebaseStorage)
        })
    }
    private var view : View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(view == null){
            _binding = RecetasBaseFragmentBinding.inflate(inflater,container,false)
            view = _binding!!.root
        }
        return view as View
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        (activity as MainActivity).configureActionBar(this)
    }

    private fun onRecetaClickListener(receta: Receta){ mainActivity.navigate(R.id.action_recetasBaseFragment_to_recetaDetalle,Bundle().apply { putSerializable("receta",receta) }) }

}