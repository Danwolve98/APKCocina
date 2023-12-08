package com.example.apkcocina.features.crearReceta.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.features.crearReceta.usecase.SendRecetaUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.CrearRecetaStateUI
import com.example.apkcocina.utils.states.SendRecetaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearRecetasViewModel @Inject constructor(
    private val sendRecetaUseCase: SendRecetaUseCase
) : ViewModel() {

    private val _crearRecetaStateUI = MutableStateFlow(CrearRecetaStateUI())
    val crearRecetaUI : StateFlow<CrearRecetaStateUI> = _crearRecetaStateUI

    private val _sendRecetaState = MutableLiveData<Event<SendRecetaState>>()
    val sendRecetaState get() = _sendRecetaState

    fun crearReceta(receta : Receta){
        viewModelScope.launch(Dispatchers.IO) {
            _crearRecetaStateUI.value = CrearRecetaStateUI(isLoading = true)
            _sendRecetaState.postValue(Event(sendRecetaUseCase(receta)))
            _crearRecetaStateUI.value = CrearRecetaStateUI(isLoading = false)
        }
    }
}