package com.example.apkcocina.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun<T : Any?> Fragment.collectFlow(stateFlow : StateFlow<T?>, lifecycleState : Lifecycle.State = Lifecycle.State.STARTED, execute : (T) -> Unit){
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState){
            stateFlow.collect{
                it.notNull {
                    execute(it!!)
                }
            }
        }
    }
}