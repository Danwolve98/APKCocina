package com.example.apkcocina.features.crearReceta.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.databinding.FrgCrearRecetaBinding
import com.example.apkcocina.features.crearReceta.adapter.AlergenosAdapter
import com.example.apkcocina.features.crearReceta.adapter.CrearDescripcionAdapter
import com.example.apkcocina.features.crearReceta.adapter.CrearProductosAdapter
import com.example.apkcocina.features.crearReceta.adapter.ViewHolderDeletable
import com.example.apkcocina.features.crearReceta.viewModel.CrearRecetasViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.base.InfoActionBar
import com.example.apkcocina.utils.dialog.InfoRecetasDialog
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.playAnimation
import com.example.apkcocina.utils.extensions.toBase64
import com.example.apkcocina.utils.model.Alergenos
import com.example.apkcocina.utils.model.Descripcion
import com.example.apkcocina.utils.model.Producto
import com.example.apkcocina.utils.states.SendRecetaState
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CrearRecetaFragment() : BaseFragment<FrgCrearRecetaBinding>(){

    @Inject
    lateinit var firebaseStorage: FirebaseStorage
    @Inject
    lateinit var firestore: FirebaseFirestore
    override lateinit var actionBar: APKCocinaActionBar

    val viewModel by viewModels<CrearRecetasViewModel>()

    private var isButtonPressed : Boolean = false

    private var productosAdapter = CrearProductosAdapter(listOf(Producto()))
    private var descripcionAdapter = CrearDescripcionAdapter(listOf(Descripcion()))

    override fun assingActionBar() {
        actionBar = InfoActionBar(getString(R.string.crear_receta),::mostrarDialogRecetas)
    }
    private fun mostrarDialogRecetas() = InfoRecetasDialog(requireContext()).show()

    override fun initializeView() {
        binding.btCrearReceta.setOnClickListener {
            it.playAnimation(R.anim.click_animation)
            crearReceta()
        }

        binding.apply {
            //ALERGENOS
            rvAlergenos.layoutManager = flexboxLayoutManager()
            rvAlergenos.adapter = AlergenosAdapter(Alergenos.values().toList())
            //PRODUCTOS
            rvProductos.adapter = productosAdapter
            btAddProducto.setOnClickListener {
                it.playAnimation(R.anim.click_animation)
                btDeleteProducto.isChecked = false
                btDeleteDescripcion.isChecked = false
                var newList = productosAdapter.listProductos
                newList = newList.toMutableList().also {
                    it.add(Producto())
                }.toList()
                productosAdapter.updateRecetas(newList.toList())
                rvProductos.smoothScrollToPosition(productosAdapter.listProductos.size-1)
            }

            btDeleteProducto.addOnCheckedChangeListener {bt,checked->
                bt.playAnimation(R.anim.click_animation)
                enableDeleteProductos(checked)
                if(productosAdapter.listProductos.isEmpty())
                    bt.isChecked=false
            }

            //MINS
            addLooperSegundos()

            //DESCRIPCION
            rvDescripcion.adapter = descripcionAdapter

            btAddText.setOnClickListener {
                it.playAnimation(R.anim.click_animation)
                var newList = descripcionAdapter.listDescripcion
                newList = newList.toMutableList().also {
                    it.add(Descripcion())
                }
                btDeleteDescripcion.isChecked = false
                btDeleteProducto.isChecked = false
                descripcionAdapter.updateDescripcion(newList)
                rvDescripcion.smoothScrollToPosition(descripcionAdapter.listDescripcion.size-1)
            }

            val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
                val image = it?.toBase64(requireContext())

                image.notNull{foto->
                    var newList = descripcionAdapter.listDescripcion
                    newList = newList.toMutableList().also {lista->
                        lista.add(Descripcion(string = foto,image = true))
                    }
                    descripcionAdapter.updateDescripcion(newList)
                    rvDescripcion.smoothScrollToPosition(descripcionAdapter.listDescripcion.size-1)
                }
            }

            btAddImage.setOnClickListener {
                it.playAnimation(R.anim.click_animation)
                btDeleteDescripcion.isChecked = false
                btDeleteProducto.isChecked = false
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btDeleteDescripcion.addOnCheckedChangeListener { bt,checked->
                bt.playAnimation(R.anim.click_animation)
                enableDeleteDescripciones(checked)
                if(descripcionAdapter.listDescripcion.isEmpty())
                    bt.isChecked = false
            }
        }
    }

    private fun enableDeleteProductos(enable : Boolean){
        val list = (binding.rvProductos.adapter as CrearProductosAdapter).listProductos

        for ((pos) in list.withIndex()){
            (binding.rvProductos.findViewHolderForAdapterPosition(pos) as CrearProductosAdapter.ViewHolder).enableBorrar(enable)
        }
    }

    private fun enableDeleteDescripciones(enable : Boolean){
        val list = (binding.rvDescripcion.adapter as CrearDescripcionAdapter).listDescripcion

        for ((pos) in list.withIndex()){
            (binding.rvDescripcion.findViewHolderForAdapterPosition(pos) as ViewHolderDeletable).enableBorrar(enable)
        }
    }

    private fun crearReceta() {
        val receta = Receta().apply {
            nombre = binding.etNombreReceta.text.toString()
            alergenos = (binding.rvAlergenos.adapter as AlergenosAdapter).getSelectedAlergenos()
            ingredientes = (binding.rvProductos.adapter as CrearProductosAdapter).listProductos
            tiempoPreparacion = tiempo
            descripcion = (binding.rvDescripcion.adapter as CrearDescripcionAdapter).listDescripcion.filter { it.string.isNotEmpty() }
        }

        viewModel.crearReceta(receta)
    }

    override fun initializeObservers() {
        collectFlow(viewModel.crearRecetaUI){
            mainActivity.setLoading(it.isLoading)
        }

        viewModel.sendRecetaState.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let {
                when(it){
                    is SendRecetaState.Error -> Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    SendRecetaState.Successfull -> Toast.makeText(requireContext(), getString(R.string.receta_creada_con_exito), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Función que controla el aumento y decremento gradual del tiempo de la receta
     */
    private fun FrgCrearRecetaBinding.addLooperSegundos() {
        val handler = Handler(Looper.myLooper()!!)

        btMinsSum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    /*etMins.setText((etMins.text.toString().toInt() + 1).toString())*/
                    isButtonPressed = true
                    startIncrementingCounter(
                        handler,
                        true
                    )
                    true
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    isButtonPressed = true
                    handler.removeCallbacksAndMessages(null)
                    true
                }

                else -> false
            }
        }

        btMinsRes.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    /*etMins.setText((etMins.text.toString().toInt() - 1).toString())*/
                    isButtonPressed = true
                    startIncrementingCounter(
                        handler,
                        false
                    )
                    true
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    isButtonPressed = false
                    handler.removeCallbacksAndMessages(null)
                    true
                }

                else -> false
            }
        }
    }
    private var tiempo = 0
    private var delayCount = 500L
    @SuppressLint("SuspiciousIndentation")
    fun startIncrementingCounter(handler : Handler, sum : Boolean) {
        handler.postDelayed({
            if (isButtonPressed) {
                if (sum)
                    tiempo++
                else
                    tiempo--

                if(delayCount > Constants.MAX_DECEREMENT_DELAY)
                    delayCount -= Constants.DECREMENT_DELAY

                binding.etMins.text = convertirMinutosAHorasYMinutos(tiempo)
                startIncrementingCounter(handler,sum)
            }
        }, delayCount)
    }

    private fun convertirMinutosAHorasYMinutos(minutos: Int): String {
        val horas = minutos / 60
        val minutosRestantes = minutos % 60

        // Formatear el resultado como hh:mm
        return String.format("%02d:%02d", horas, minutosRestantes)
    }

    private fun flexboxLayoutManager() =
        object : FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP) {
            init {
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START
            }

            override fun getFlexItemAt(index: Int): View {
                val item = super.getFlexItemAt(index)

                val params = item.layoutParams as LayoutParams
                params.flexBasisPercent = 0.2f
                params.isWrapBefore = index > 0 && index % 5 == 0

                return item
            }
        }
}