package com.example.apkcocina.features.recetasBase.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.recetasBase.usecase.GetRecetasBaseUseCase
import com.example.apkcocina.network.usecases.CargarUsuarioUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.states.CrearRecetaStateUI
import com.example.apkcocina.utils.states.RecetasOnlineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecetasBaseViewModel @Inject constructor(
    private val getRecetasBaseUseCase: GetRecetasBaseUseCase
) : ViewModel(){

    private val _recetas = MutableLiveData<Event<List<Receta>>>()
    val recetas: LiveData<Event<List<Receta>>> = _recetas

    private val _recetasError = MutableLiveData<Event<String>>()
    val recetasError: LiveData<Event<String>> = _recetasError

    private val _loading = MutableStateFlow(CrearRecetaStateUI())
    val loading : StateFlow<CrearRecetaStateUI> = _loading

    fun loadRecetas(){
        viewModelScope.launch{
            _loading.value = CrearRecetaStateUI(isLoading = true)
            when(val result = getRecetasBaseUseCase()){
                is RecetasOnlineState.Error -> { _recetasError.value = Event(result.error) }
                is RecetasOnlineState.Successfull -> { _recetas.value = Event(result.listRecetas) }
                else ->{}
            }
            _loading.value = CrearRecetaStateUI(isLoading = false)
        }
    }

}