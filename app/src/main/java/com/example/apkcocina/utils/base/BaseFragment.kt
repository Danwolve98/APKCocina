package com.example.apkcocina.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<vb : ViewBinding> : Fragment() {

    abstract var actionBar : APKCocinaActionBar
    lateinit var binding : vb
    private val navigator : NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createBindingInstance(inflater,container).root
    }

    protected open fun createBindingInstance(inflater: LayoutInflater, container: ViewGroup?): vb {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<vb>
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        binding = method.invoke(null, inflater, container, false) as vb
        return binding
    }

    protected fun navigate(action : Int) = navigator.navigate(action)
    protected fun navigate(navDirections: NavDirections) = navigator.navigate(navDirections)
}