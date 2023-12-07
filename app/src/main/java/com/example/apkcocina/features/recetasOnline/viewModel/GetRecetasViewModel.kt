package com.example.apkcocina.features.recetasOnline.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.features.recetasOnline.useCase.GetRecetasOnlineUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.LoginResult
import com.example.apkcocina.utils.states.RecetasOnlineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRecetasViewModel @Inject constructor(
   private val getRecetasOnlineUseCase: GetRecetasOnlineUseCase
) : ViewModel() {

    private val _recetasResult = MutableLiveData<Event<List<Receta>>>()
    val recetasResult: LiveData<Event<List<Receta>>> = _recetasResult

    private val _recetasError = MutableLiveData<Event<String>>()
    val recetasError: LiveData<Event<String>> = _recetasError

    fun getRecetas() = viewModelScope.launch {
        when(val result = getRecetasOnlineUseCase() ){
            is RecetasOnlineState.Error -> _recetasError.value = Event(result.error)
            is RecetasOnlineState.Successfull -> _recetasResult.value = Event(result.listRecetas)
        }
    }
}