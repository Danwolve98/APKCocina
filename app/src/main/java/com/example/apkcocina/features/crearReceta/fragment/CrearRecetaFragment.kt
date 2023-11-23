package com.example.apkcocina.features.crearReceta.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.databinding.FrgCrearRecetaBinding
import com.example.apkcocina.features.crearReceta.adapter.CrearProductosAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.Constants
import com.example.apkcocina.utils.base.InfoActionBar
import com.example.apkcocina.utils.dialog.InfoRecetasDialog
import com.example.apkcocina.utils.model.Alergenos
import com.example.apkcocina.utils.model.Producto
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

    private lateinit var receta : Receta
    private var isButtonPressed : Boolean = false

    private var productosAdapter = CrearProductosAdapter(listOf(Producto()))

    override fun assingActionBar() {
        actionBar = InfoActionBar(getString(R.string.crear_receta),::mostrarDialogRecetas)
    }
    private fun mostrarDialogRecetas() = InfoRecetasDialog(requireContext()).show()

    override fun initializeView() {
        binding.btCrearReceta.setOnClickListener {
            val receta = Receta().apply {
                descripcion = "COMIDA RICA EN POTASIO"
                nombre = "POTATSIO PLATANOSO"
                alergenos = listOf(Alergenos.ALTRAMUCES, Alergenos.APIO, Alergenos.GLUTEN)
                ingredientes = hashMapOf<String, String>().apply {
                    put("papata", "2")
                    put("potatsio", "1g")
                }
                tiempoPreparacion = 70
                imagenes = listOf()
            }

            val collection = firestore.collection(Receta.RECETAS_USUARIOS)

            collection.add(receta).addOnSuccessListener { documentReference ->
                Log.d("TAG", "Documento agregado con ID: ${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.e("TAG", "Error al agregar el documento", e)
                }
        }

        binding.apply {
            addLooperSegundos()

            rvProductos.adapter = productosAdapter

            btAddProducto.setOnClickListener {
                var newList = productosAdapter.listProductos
                newList = newList.toMutableList().also {
                 it.add(Producto())
                }.toList()
                productosAdapter.updateRecetas(newList.toList())
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
                    etMins.setText((etMins.text.toString().toInt() + 1).toString())
                    isButtonPressed = true
                    startIncrementingCounter(
                        handler,
                        binding.etMins.text.toString().toInt(),
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
                    etMins.setText((etMins.text.toString().toInt() - 1).toString())
                    isButtonPressed = true
                    startIncrementingCounter(
                        handler,
                        binding.etMins.text.toString().toInt(),
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

    var delayCount = 500L
    @SuppressLint("SuspiciousIndentation")
    fun startIncrementingCounter(handler : Handler, tiempo : Int, sum : Boolean) {
        var counter = tiempo
        handler.postDelayed({
            if (isButtonPressed) {
                if (sum)
                    counter++
                else
                    counter--

                if(delayCount > Constants.MAX_DECEREMENT_DELAY)
                delayCount -= Constants.DECREMENT_DELAY

                binding.etMins.setText(counter.toString())
                startIncrementingCounter(handler,counter,sum)
            }
        }, delayCount)
    }

}