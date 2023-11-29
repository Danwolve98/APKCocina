package com.example.apkcocina.features.crearReceta.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.databinding.FrgCrearRecetaBinding
import com.example.apkcocina.features.crearReceta.adapter.AlergenosAdapter
import com.example.apkcocina.features.crearReceta.adapter.CrearProductosAdapter
import com.example.apkcocina.features.crearReceta.viewModel.CrearRecetasViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.base.InfoActionBar
import com.example.apkcocina.utils.dialog.InfoRecetasDialog
import com.example.apkcocina.utils.model.Alergenos
import com.example.apkcocina.utils.model.Producto
import com.example.apkcocina.utils.states.CrearRecetaState
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexLine
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
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

    override fun assingActionBar() {
        actionBar = InfoActionBar(getString(R.string.crear_receta),::mostrarDialogRecetas)
    }
    private fun mostrarDialogRecetas() = InfoRecetasDialog(requireContext()).show()

    override fun initializeView() {
        binding.btCrearReceta.setOnClickListener { crearReceta() }

        binding.apply {
            //ALERGENOS
            rvAlergenos.layoutManager = flexboxLayoutManager()
            rvAlergenos.adapter = AlergenosAdapter(Alergenos.values().toList())
            //PRODUCTOS
            rvProductos.adapter = productosAdapter
            btAddProducto.setOnClickListener {
                var newList = productosAdapter.listProductos
                newList = newList.toMutableList().also {
                    it.add(Producto())
                }.toList()
                productosAdapter.updateRecetas(newList.toList())
            }
            //MINS
            addLooperSegundos()

        }
    }

    private fun crearReceta() {

        val receta = Receta().apply {
            descripcion = "COMIDA RICA EN POTASIO"
            nombre = binding.etNombreReceta.text.toString()
            alergenos = (binding.rvAlergenos.adapter as AlergenosAdapter).getSelectedAlergenos()
            ingredientes = (binding.rvProductos.adapter as CrearProductosAdapter).listProductos
            tiempoPreparacion = tiempo
            imagenes = listOf()
        }

        viewModel.crearReceta(receta)
    }

    override fun initializeObservers() {
        viewModel._sendRecetaState.observe(viewLifecycleOwner){
            when(it){
                is CrearRecetaState.Error -> Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                CrearRecetaState.Successfull -> Toast.makeText(requireContext(), getString(R.string.receta_creada_con_exito), Toast.LENGTH_SHORT).show()
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

    fun convertirMinutosAHorasYMinutos(minutos: Int): String {
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