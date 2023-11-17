package com.example.apkcocina.utils.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.apkcocina.APKCocinaActivity
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.hideKeyboard
import com.example.apkcocina.utils.extensions.notNullorDefault
import java.lang.reflect.ParameterizedType

/**
 * Fragment genérico para todos los demás fragments, implementa toda la lógica necesaria para su correcto funcionamiento entre sí y con el
 * [APKCocinaActivity] principal
 */
abstract class BaseFragment<vb : ViewBinding> : Fragment() {

    //ACTION BAR SUPERIOR
    abstract var actionBar: APKCocinaActionBar
    lateinit var binding: vb
    val APKCocinaActivity: APKCocinaActivity get() = requireActivity() as APKCocinaActivity

    //NAVCONTROLLER
    val navController: NavController by lazy { findNavController() }

    //SI CONTROLA ONBACKPRESSED POR DEFECTO EL FRAGMENT
    open var addOnBackPressed = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    /**
     * Función que te obliga a inicializar un actionBar
     */
    abstract fun assingActionBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (addOnBackPressed) {
            APKCocinaActivity.onBackPressedDispatcher.addCallback(this) {
                onBackPressed()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBindingInstance(inflater, container)
        initializeObservers()
        assingActionBar()
        actionBar.notNullorDefault(
            notNullAction = { APKCocinaActivity.configureActionBar(actionBar) },
            nullAction = {APKCocinaActivity.configureActionBar(TitleActionBar(getString(R.string.app_name)))}
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    /**
     * Función que se encargar de inicializar las vistas en [onViewCreated]
     */
    open fun initializeView() {}

    /**
     * Función que se encargar se iniciar escuchadores, observadores o collects provenientes de un viewModel en [onCreate]
     */
    open fun initializeObservers() {}

    /**
     * Función que se encarga de recoger el metodo inflate de un Binding y crear su vista
     */
    protected open fun createBindingInstance(inflater: LayoutInflater, container: ViewGroup?): vb {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<vb>
        val method = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        return method.invoke(null, inflater, container, false) as vb
    }

    /**
     * Función agregada para reemplazar el onBackPressed de la actividad, cerrarra pestañas que no queramos tener abiertas antes de navegar hacia atrás
     */
    open fun onBackPressed() {
        binding.root.hideKeyboard()
        navController.navigateUp()
    }

    /**
     * Navega hace un destino por ID[Int] y puede adjuntar un bundle (nulo por defecto)
     */
    protected fun navigate(action: Int, bundle: Bundle? = null) =
        navController.navigate(action, bundle)

    /**
     * Navega hace un destino por [NavDirections]
     */
    protected fun navigate(navDirections: NavDirections) = navController.navigate(navDirections)

}