package com.example.apkcocina.features.crearReceta.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.features.crearReceta.usecase.SendRecetaUseCase
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.CrearRecetaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearRecetasViewModel @Inject constructor(
    private val sendRecetaUseCase: SendRecetaUseCase
) : ViewModel() {

    private val sendRecetaState = MutableLiveData<CrearRecetaState>()
    val _sendRecetaState get() = sendRecetaState

    fun crearReceta(receta : Receta){
        viewModelScope.launch(Dispatchers.IO) {
            sendRecetaState.postValue(sendRecetaUseCase(receta))
        }
    }
}